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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Message;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.jms.container.Binder;
import org.seasar.jms.container.annotation.JMSHeader;
import org.seasar.jms.container.annotation.JMSPayload;
import org.seasar.jms.container.annotation.JMSProperty;
import org.seasar.jms.container.annotation.OnMessage;
import org.seasar.jms.container.exception.IllegalMessageHandlerException;
import org.seasar.jms.container.exception.MessageHandlerNotFoundException;

public class MessageListenerSupport {
    public static final String DEFAULT_MESSAGE_HANDLER_NAME = "onMessage";

    protected final List<Binder> binders = new ArrayList<Binder>();
    protected Method method;

    public MessageListenerSupport(final Class<?> clazz) {
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
        setupBinderFromProperty(beanDesc);
        setupBinderFromField(beanDesc);
        setupListenerMethod(clazz);
    }

    public void bind(final Object target, final Message message, final Object payload) {
        for (final Binder binder : binders) {
            binder.bind(target, message, payload);
        }
    }

    public void invoke(final Object target) {
        MethodUtil.invoke(method, target, null);
    }

    public String getListenerMethodName() {
        return method.getName();
    }

    protected void setupBinderFromProperty(final BeanDesc beanDesc) {
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            final PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            final Method method = propertyDesc.getWriteMethod();
            if (method == null) {
                continue;
            }

            final JMSHeader header = method.getAnnotation(JMSHeader.class);
            if (header != null) {
                final BindingType bindingType = header.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(header.name()) ? propertyDesc
                            .getPropertyName() : header.name();
                    binders.add(new JMSHeaderBinder(name, bindingType, propertyDesc));
                }
            }

            final JMSProperty property = method.getAnnotation(JMSProperty.class);
            if (property != null) {
                final BindingType bindingType = property.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(property.name()) ? propertyDesc
                            .getPropertyName() : property.name();
                    binders.add(new JMSPropertyBinder(name, bindingType, propertyDesc));
                }
            }

            final JMSPayload payload = method.getAnnotation(JMSPayload.class);
            if (payload != null) {
                final BindingType bindingType = payload.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(payload.name()) ? propertyDesc
                            .getPropertyName() : payload.name();
                    binders.add(new JMSPayloadBinder(name, bindingType, propertyDesc));
                }
            }
        }
    }

    protected void setupBinderFromField(final BeanDesc beanDesc) {
        for (int i = 0; i < beanDesc.getFieldSize(); ++i) {
            final Field field = beanDesc.getField(i);

            final JMSHeader header = field.getAnnotation(JMSHeader.class);
            if (header != null) {
                final BindingType bindingType = header.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(header.name()) ? field.getName()
                            : header.name();
                    binders.add(new JMSHeaderBinder(name, bindingType, field));
                }
            }

            final JMSProperty property = field.getAnnotation(JMSProperty.class);
            if (property != null) {
                final BindingType bindingType = property.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(property.name()) ? field.getName()
                            : property.name();
                    binders.add(new JMSPropertyBinder(name, bindingType, field));
                }
            }

            final JMSPayload payload = field.getAnnotation(JMSPayload.class);
            if (payload != null) {
                final BindingType bindingType = payload.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(payload.name()) ? field.getName()
                            : payload.name();
                    binders.add(new JMSPayloadBinder(name, bindingType, field));
                }
            }
        }
    }

    protected void setupListenerMethod(final Class<?> clazz) {
        for (Class<?> type = clazz; type != Object.class; type = type.getSuperclass()) {
            for (final Method method : type.getDeclaredMethods()) {
                final OnMessage annotation = method.getAnnotation(OnMessage.class);
                if (annotation == null) {
                    continue;
                }
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalMessageHandlerException("EJMS2004", new Object[] { method });
                }
                method.setAccessible(true);
                this.method = method;
                return;
            }
        }

        try {
            final Method method = ClassUtil.getDeclaredMethod(clazz, DEFAULT_MESSAGE_HANDLER_NAME,
                    null);
            method.setAccessible(true);
            this.method = method;
        } catch (final NoSuchMethodRuntimeException e) {
            throw new MessageHandlerNotFoundException(clazz.getName());
        }
    }
}
