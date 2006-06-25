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

import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.jms.container.annotation.OnMessage;
import org.seasar.jms.container.exception.IllegalMessageHandlerException;
import org.seasar.jms.container.exception.MessageHandlerNotFoundException;

public class MessageListenerSupport {
    public static final String DEFAULT_MESSAGE_HANDLER_NAME = "onMessage";

    protected Method method;

    public MessageListenerSupport(final Class<?> clazz) {
        setupListenerMethod(clazz);
    }

    public void invoke(final Object target) {
        MethodUtil.invoke(method, target, null);
    }

    public String getListenerMethodName() {
        return method.getName();
    }

    protected void setupListenerMethod(final Class<?> clazz) {
        for (Class<?> type = clazz; type != Object.class; type = type
                .getSuperclass()) {
            for (final Method method : type.getDeclaredMethods()) {
                final OnMessage annotation = method
                        .getAnnotation(OnMessage.class);
                if (annotation == null) {
                    continue;
                }
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalMessageHandlerException("EJMS2004",
                            new Object[] { method });
                }
                method.setAccessible(true);
                this.method = method;
                return;
            }
        }

        try {
            final Method method = ClassUtil.getDeclaredMethod(clazz,
                    DEFAULT_MESSAGE_HANDLER_NAME, null);
            method.setAccessible(true);
            this.method = method;
        } catch (final NoSuchMethodRuntimeException e) {
            throw new MessageHandlerNotFoundException(clazz.getName());
        }
    }
}
