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
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.JmsContainer;
import org.seasar.jms.container.MessageBinder;
import org.seasar.jms.container.MessageBinderFactory;
import org.seasar.jms.container.annotation.MessageHandler;
import org.seasar.jms.core.exception.NotSupportedMessageRuntimeException;

/**
 * @author y-komori
 * 
 */
@Component(instance = InstanceType.PROTOTYPE)
public class JmsContainerImpl implements JmsContainer {
    private static final String DEFAULT_MESSAGE_HANDLER_NAME = "onMessage";
    private Object messageHandler;
    private MessageBinderFactory messageBinderFactory = new MessageBinderFactoryImpl();
    private static Logger logger = Logger.getLogger(JmsContainerImpl.class);

    public void onMessage(Message message) {
        logger.debug("[S2JMS-Container] onMessage called.");

        Method messageHandlerMethod = findMessageHandler(messageHandler.getClass());
        if (messageHandlerMethod != null) {
            bindMessage(message);
            invokeMessageaHandler(messageHandlerMethod.getName());
        }
    }

    private void bindMessage(Message message) {
        MessageBinder binder = messageBinderFactory.getMessageBinder(message);
        if (binder != null) {
            binder.bindMessage(messageHandler, message);
        } else {
            throw new NotSupportedMessageRuntimeException("EJMS2001", message);
        }
    }

    private void invokeMessageaHandler(String methodName) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(messageHandler.getClass());
        if (beanDesc != null) {
            beanDesc.invoke(messageHandler, methodName, null);
        }
    }

    private Method findMessageHandler(Class clazz) {
        Method messageHandlerMethod = null;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            MessageHandler annotation = method.getAnnotation(MessageHandler.class);
            if (annotation != null) {
                messageHandlerMethod = method;
                break;
            }
        }

        if (messageHandlerMethod == null) {
            try {
                messageHandlerMethod = clazz.getDeclaredMethod(DEFAULT_MESSAGE_HANDLER_NAME,
                        (Class[]) null);
            } catch (NoSuchMethodException e) {
                // Don't care.
            }
        }

        return messageHandlerMethod;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setMessageHandler(Object messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setMessageBinderFactory(MessageBinderFactory messageBinderFactory) {
        this.messageBinderFactory = messageBinderFactory;
    }
}
