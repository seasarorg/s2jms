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
package org.seasar.jms.core.interceptor;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.jca.util.ReflectionUtil;
import org.seasar.jms.core.message.MessageFactory;
import org.seasar.jms.core.message.impl.BytesMessageFactory;
import org.seasar.jms.core.message.impl.MapMessageFactory;
import org.seasar.jms.core.message.impl.ObjectMessageFactory;
import org.seasar.jms.core.message.impl.TextMessageFactory;

/**
 * @author koichik
 */
@Component
public class SendReturnValueInterceptor extends AbstractSendMessageInterceptor {
    protected Map<Class<?>, Class<? extends MessageFactory>> factories = new LinkedHashMap<Class<?>, Class<? extends MessageFactory>>();

    public SendReturnValueInterceptor() {
        factories.put(String.class, TextMessageFactory.class);
        factories.put(byte[].class, BytesMessageFactory.class);
        factories.put(Map.class, MapMessageFactory.class);
        factories.put(Serializable.class, ObjectMessageFactory.class);
    }

    public void clearFactories() {
        factories.clear();
    }

    public void addMessageFactory(final Class<?> payloadClass,
            final Class<? extends MessageFactory> messageFactoryClass) {
        factories.put(payloadClass, messageFactoryClass);
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object result = proceed(invocation);
        if (result != null) {
            getMessageSender().send(createMessageFactory(result));
        }
        return result;
    }

    protected MessageFactory<?> createMessageFactory(final Object returnValue) {
        final Class<?> returnType = returnValue.getClass();
        for (Class<?> payloadType : factories.keySet()) {
            if (payloadType.isAssignableFrom(returnType)) {
                final Class<? extends MessageFactory> factoryClass = factories.get(payloadType);
                final Constructor<? extends MessageFactory> ctor = ReflectionUtil.getConstructor(
                        factoryClass, payloadType);
                return ReflectionUtil.newInstance(ctor, returnValue);
            }
        }
        throw new SIllegalStateException("EJMS1003", new Object[] { returnType });
    }
}
