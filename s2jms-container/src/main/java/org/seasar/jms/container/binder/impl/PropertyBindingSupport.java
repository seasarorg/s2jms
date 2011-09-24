/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.jms.container.binder.BindingSupport;

/**
 * JMSメッセージをリスナコンポーネントのプロパティにバインドするコンポーネントです。
 * 
 * @author koichik
 */
public class PropertyBindingSupport implements BindingSupport {

    // instance fields
    /** JMSメッセージをバインドする対象のプロパティ */
    protected PropertyDesc property;

    /**
     * インスタンスを構築します。
     * 
     * @param property
     *            JMSメッセージをバインドする対象のプロパティ
     */
    public PropertyBindingSupport(final PropertyDesc property) {
        this.property = property;
    }

    public void bind(final Object target, final Object value) {
        property.setValue(target, value);
    }

}
