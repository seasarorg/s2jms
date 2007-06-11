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

import javax.jms.JMSException;
import javax.jms.Message;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.core.exception.SJMSRuntimeException;

public class JMSPropertyBinder extends AbstractBinder {
    public JMSPropertyBinder(final String name, final BindingType bindingType,
            final PropertyDesc property) {
        super(name, bindingType, new PropertyBindingSupport(property));
    }

    public JMSPropertyBinder(final String name, final BindingType bindingType, final Field field) {
        super(name, bindingType, new FieldBindingSupport(field));
    }

    @Override
    protected boolean doBind(Object target, Message message, Object payload) {
        try {
            final Object property = message.getObjectProperty(name);
            if (property == null) {
                return false;
            }
            bindingSupport.bind(target, property);
            return true;
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }
}
