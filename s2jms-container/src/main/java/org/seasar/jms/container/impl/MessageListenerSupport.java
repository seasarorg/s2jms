/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.jms.container.impl;

import java.lang.reflect.Method;

import javax.jms.Message;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.annotation.OnMessage;
import org.seasar.jms.container.exception.IllegalMessageListenerException;
import org.seasar.jms.container.exception.MessageListenerNotFoundException;
import org.seasar.jms.container.exception.NotSupportedMessageException;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.MessageHandlerFactory;

public class MessageListenerSupport {

    public static final String DEFAULT_MESSAGE_HANDLER_NAME = "onMessage";

    private static Logger logger = Logger.getLogger(JMSContainerImpl.class);

    protected Method method;

    protected ParameterBuilder parameterBuilder;

    public MessageListenerSupport(final Class<?> clazz) {
        setupListenerMethod(clazz);
    }

    public void invoke(final Object target, final Message message) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.log("DJMS-CONTAINER2103", new Object[] { method });
        }
        try {
            final Object[] parameters = parameterBuilder.build(message);
            method.invoke(target, parameters);
        } finally {
            if (logger.isDebugEnabled()) {
                logger.log("DJMS-CONTAINER2104", new Object[] { method });
            }
        }
    }

    public String getListenerMethodName() {
        return method.getName();
    }

    protected void setupListenerMethod(final Class<?> clazz) {
        for (final Method method : clazz.getMethods()) {
            final OnMessage annotation = method.getAnnotation(OnMessage.class);
            if (annotation == null) {
                continue;
            }
            parameterBuilder = getParameterBuilder(method);
            this.method = method;
            return;
        }

        try {
            final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
            final Method[] methods = beanDesc.getMethods(DEFAULT_MESSAGE_HANDLER_NAME);
            for (int i = 0; i < methods.length; ++i) {
                final Method method = methods[i];
                final Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length <= 1) {
                    parameterBuilder = getParameterBuilder(method);
                    this.method = method;
                    return;
                }
            }
        } catch (final MethodNotFoundRuntimeException ignore) {
        }
        throw new MessageListenerNotFoundException(clazz);
    }

    protected ParameterBuilder getParameterBuilder(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return new EmptyBuilder();
        }
        if (parameterTypes.length == 1) {
            final Class<?> parameterType = parameterTypes[0];
            if (Message.class.isAssignableFrom(parameterType)) {
                return new MessageBuilder();
            }
            return new PayloadBuilder(parameterType);
        }
        throw new IllegalMessageListenerException(method);
    }

    public interface ParameterBuilder {
        Object[] build(Message message);
    }

    public static class EmptyBuilder implements ParameterBuilder {
        protected static final Object[] EMPTY_ARRAY = new Object[0];

        public Object[] build(final Message message) {
            return EMPTY_ARRAY;
        }
    }

    public static class MessageBuilder implements ParameterBuilder {
        public Object[] build(final Message message) {
            return new Object[] { message };
        }
    }

    public static class PayloadBuilder implements ParameterBuilder {
        protected Class<?> payloadType;

        public PayloadBuilder(final Class<?> payloadType) {
            this.payloadType = payloadType;
        }

        @SuppressWarnings("unchecked")
        public Object[] build(final Message message) {
            final MessageHandler handler = MessageHandlerFactory
                    .getMessageHandlerFromPayloadType(payloadType);
            if (handler == null) {
                throw new NotSupportedMessageException(message);
            }
            return new Object[] { handler.handleMessage(message) };
        }
    }
}
