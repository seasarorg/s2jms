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
package org.seasar.jms.core.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.Message;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link javax.jms.Message}のヘッダー情報にアクセスするためのユーティリティ。
 * 
 * @author koichik
 */
public class JMSHeaderSupport {

    // static fields
    /** JMSメッセージのヘッダ名の{@link Set} */
    protected static final Set<String> headerNames;

    /** JMSメッセージのヘッダ名と{@link Message}からその値を取得するgetterメソッドの{@link Map} */
    protected static final Map<String, Method> getterMethods = CollectionsUtil
            .newHashMap();

    /** JMSメッセージのヘッダ名と{@link Message}にその値を設定するsetterメソッドの{@link Map} */
    protected static final Map<String, Method> setterMethods = CollectionsUtil
            .newLinkedHashMap();

    static {
        getterMethods.put("JMSCorrelationID", ClassUtil.getMethod(
                Message.class, "getJMSCorrelationID", null));
        getterMethods.put("JMSCorrelationIDAsBytes", ClassUtil.getMethod(
                Message.class, "getJMSCorrelationIDAsBytes", null));
        getterMethods.put("JMSDeliveryMode", ClassUtil.getMethod(Message.class,
                "getJMSDeliveryMode", null));
        getterMethods.put("JMSDestination", ClassUtil.getMethod(Message.class,
                "getJMSDestination", null));
        getterMethods.put("JMSExpiration", ClassUtil.getMethod(Message.class,
                "getJMSExpiration", null));
        getterMethods.put("JMSMessageID", ClassUtil.getMethod(Message.class,
                "getJMSMessageID", null));
        getterMethods.put("JMSPriority", ClassUtil.getMethod(Message.class,
                "getJMSPriority", null));
        getterMethods.put("JMSRedelivered", ClassUtil.getMethod(Message.class,
                "getJMSRedelivered", null));
        getterMethods.put("JMSReplyTo", ClassUtil.getMethod(Message.class,
                "getJMSReplyTo", null));
        getterMethods.put("JMSTimestamp", ClassUtil.getMethod(Message.class,
                "getJMSTimestamp", null));
        getterMethods.put("JMSType", ClassUtil.getMethod(Message.class,
                "getJMSType", null));

        setterMethods.put("JMSCorrelationID", ClassUtil.getMethod(
                Message.class, "setJMSCorrelationID", new Class<?>[] {String.class}));
        setterMethods.put("JMSCorrelationIDAsBytes", ClassUtil.getMethod(
                Message.class, "setJMSCorrelationIDAsBytes", new Class<?>[] {byte[].class}));
        setterMethods.put("JMSDeliveryMode", ClassUtil.getMethod(Message.class,
                "setJMSDeliveryMode", new Class<?>[] {int.class}));
        setterMethods.put("JMSDestination", ClassUtil.getMethod(Message.class,
                "setJMSDestination", new Class<?>[] {Destination.class}));
        setterMethods.put("JMSExpiration", ClassUtil.getMethod(Message.class,
                "setJMSExpiration", new Class<?>[] {long.class}));
        setterMethods.put("JMSMessageID", ClassUtil.getMethod(Message.class,
                "setJMSMessageID", new Class<?>[] {String.class}));
        setterMethods.put("JMSPriority", ClassUtil.getMethod(Message.class,
                "setJMSPriority", new Class<?>[] {int.class}));
        setterMethods.put("JMSRedelivered", ClassUtil.getMethod(Message.class,
                "setJMSRedelivered", new Class<?>[] {boolean.class}));
        setterMethods.put("JMSReplyTo", ClassUtil.getMethod(Message.class,
                "setJMSReplyTo", new Class<?>[] {Destination.class}));
        setterMethods.put("JMSTimestamp", ClassUtil.getMethod(Message.class,
                "setJMSTimestamp", new Class<?>[] {long.class}));
        setterMethods.put("JMSType", ClassUtil.getMethod(Message.class,
                "setJMSType", new Class<?>[] {String.class}));

        headerNames = Collections.unmodifiableSet(getterMethods.keySet());
    }

    /**
     * {@link javax.jms.Message}ヘッダーの名前を要素とする変更不可能な{java.util.Set}を返します。
     * 
     * @return {@link javax.jms.Message}ヘッダーの名前を要素とする変更不可能な{java.util.Set}
     */
    public static Set<String> getNames() {
        return headerNames;
    }

    /**
     * <code>message</code>の<code>name</code>で指定されたヘッダーの値を返します。
     * 
     * @param message
     *            メッセージ
     * @param name
     *            ヘッダー名
     * @return <code>message</code>の<code>name</code>で指定されたヘッダーの値
     */
    public static Object getValue(final Message message, final String name) {
        if (!getterMethods.containsKey(name)) {
            return null;
        }
        final Method getter = getterMethods.get(name);
        return MethodUtil.invoke(getter, message, null);
    }

    /**
     * <code>message</code>の<code>name</code>で指定されたヘッダーの値を設定します。
     * 
     * @param message
     *            メッセージ
     * @param name
     *            ヘッダー名
     * @param value
     *            ヘッダーに設定する値
     */
    public static void setValue(final Message message, final String name, final Object value) {
        if (!setterMethods.containsKey(name)) {
            return;
        }
        final Method setter = setterMethods.get(name);
        MethodUtil.invoke(setter, message, new Object[] {value});
    }

}
