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

import org.seasar.framework.util.FieldUtil;
import org.seasar.jms.container.binder.BindingSupport;

/**
 * JMSメッセージをリスナコンポーネントのフィールドにバインドするコンポーネントです。
 * 
 * @author koichik
 */
public class FieldBindingSupport implements BindingSupport {

    // instance fields
    /** JMSメッセージをバインドする対象のフィールド */
    protected final Field field;

    /**
     * インスタンスを構築します。
     * 
     * @param field
     *            JMSメッセージをバインドする対象のフィールド
     */
    public FieldBindingSupport(final Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    public void bind(final Object target, final Object value) {
        FieldUtil.set(field, target, value);
    }

}
