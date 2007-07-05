/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
import java.lang.reflect.Method;

import javax.jms.Message;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;

/**
 * JMSメッセージのヘッダをリスナコンポーネントにバインドするクラスです。
 * 
 * @author koichik
 */
public class JMSHeaderBinder extends AbstractBinder {

    // instance fields
    /** JMSメッセージのヘッダを取得するgetterメソッド */
    protected Method getterMethod;

    /**
     * インスタンスを構築します。
     * 
     * @param name
     *            バインドするJMSメッセージのヘッダ名
     * @param bindingType
     *            バインディングタイプ
     * @param property
     *            JMSメッセージをバインドする対象のプロパティ
     */
    public JMSHeaderBinder(final String name, final BindingType bindingType,
            final PropertyDesc property) {
        super(name, bindingType, new PropertyBindingSupport(property));
        setupGetterMethod();
    }

    /**
     * インスタンスを構築します。
     * 
     * @param name
     *            バインドするJMSメッセージのヘッダ名
     * @param bindingType
     *            バインディングタイプ
     * @param field
     *            JMSメッセージをバインドする対象のフィールド
     */
    public JMSHeaderBinder(final String name, final BindingType bindingType, final Field field) {
        super(name, bindingType, new FieldBindingSupport(field));
        setupGetterMethod();
    }

    /**
     * JMSメッセージからヘッダを取得するメソッドを準備します。
     */
    protected void setupGetterMethod() {
        final String methodName = "get"
                + ((name.startsWith("JMS")) ? name : "JMS" + StringUtil.capitalize(name));
        getterMethod = ClassUtil.getDeclaredMethod(Message.class, methodName, null);
    }

    @Override
    protected boolean doBind(final Object target, final Message message, final Object payload) {
        final Object header = MethodUtil.invoke(getterMethod, message, null);
        if (header == null) {
            return false;
        }
        bindingSupport.bind(target, header);
        return true;
    }

}
