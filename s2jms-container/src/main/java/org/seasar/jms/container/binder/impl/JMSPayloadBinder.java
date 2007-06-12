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
package org.seasar.jms.container.binder.impl;

import java.lang.reflect.Field;
import java.util.Map;

import javax.jms.Message;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.annotation.tiger.BindingType;

/**
 * JMSメッセージのペイロードをリスナコンポーネントにバインドするクラスです。
 * 
 * @author koichik
 */
public class JMSPayloadBinder extends AbstractBinder {

    // instance fields
    /** JMSメッセージのペイロード型 */
    protected Class<?> type;

    /**
     * インスタンスを構築します。
     * 
     * @param name
     *            バインドするJMSメッセージのペイロード名
     * @param bindingType
     *            バインディングタイプ
     * @param property
     *            JMSメッセージをバインドする対象のプロパティ
     */
    public JMSPayloadBinder(final String name, final BindingType bindingType,
            final PropertyDesc property) {
        super(name, bindingType, new PropertyBindingSupport(property));
        type = property.getPropertyType();
    }

    /**
     * インスタンスを構築します。
     * 
     * @param name
     *            バインドするJMSメッセージのペイロード名
     * @param bindingType
     *            バインディングタイプ
     * @param field
     *            JMSメッセージをバインドする対象のフィールド
     */
    public JMSPayloadBinder(final String name, final BindingType bindingType, final Field field) {
        super(name, bindingType, new FieldBindingSupport(field));
        type = field.getType();
    }

    @Override
    protected boolean doBind(final Object target, final Message message, final Object payload) {
        if (type.isAssignableFrom(payload.getClass())) {
            bindingSupport.bind(target, payload);
            return true;
        }
        if (payload instanceof Map) {
            final Map<?, ?> map = Map.class.cast(payload);
            final Object value = map.get(name);
            if (value != null) {
                bindingSupport.bind(target, value);
                return true;
            }
        }
        return false;
    }

}