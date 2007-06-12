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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.util.MethodUtil;
import org.seasar.jms.core.MessageSender;

/**
 * JMSメッセージを送信するインターセプタの抽象基底クラスです。
 * <p>
 * このサブクラスを使用することにより、透過的にJMSメッセージを送信することができます。
 * </p>
 * 
 * @author koichik
 */
public abstract class AbstractSendMessageInterceptor implements MethodInterceptor {

    // instance fields
    /** S2コンテナ */
    protected S2Container container;

    /** {@link MessageSender}のコンポーネント名 */
    protected String messageSenderName;

    /** {@link MessageSender}のコンポーネント定義 */
    protected ComponentDef componentDef;

    /**
     * インスタンスを構築します。
     */
    public AbstractSendMessageInterceptor() {
    }

    /**
     * S2コンテナを設定します(必須)。
     * 
     * @param container
     *            S2コンテナ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setContainer(final S2Container container) {
        this.container = container;
    }

    /**
     * JMSメッセージの送信で使用する{@link org.seasar.jms.core.MessageSender}のコンポーネント名を設定します。
     * <p>
     * このプロパティが設定されていない場合は{@link org.seasar.jms.core.MessageSender}インタフェースをキーとします。
     * 
     * @param messageSenderName
     *            JMSメッセージの送信で使用する{@link org.seasar.jms.core.MessageSender}のコンポーネント名
     */
    @Binding(bindingType = BindingType.MAY)
    public void setMessageSenderName(final String messageSenderName) {
        this.messageSenderName = messageSenderName;
    }

    /**
     * コンポーネントを初期化します。
     * <p>
     * JMSメッセージの送信で使用する{@link org.seasar.jms.core.MessageSender}のコンポーネント定義をS2コンテナからルックアップします。
     * </p>
     * 
     */
    @InitMethod
    public void initialize() {
        componentDef = container.getComponentDef(messageSenderName == null ? MessageSender.class
                : messageSenderName);
    }

    /**
     * ターゲットのメソッドを呼び出します。
     * <p>
     * ターゲットのメソッドが抽象メソッドの場合は呼び出しを行わず、{@code null}を返します。
     * 
     * @param invocation
     *            ターゲットメソッドの呼び出しを表現するオブジェクト
     * @return ターゲットメソッドの戻り値
     * @throws Throwable
     *             ターゲットメソッドが例外をスローした場合にスローされます
     */
    protected Object proceed(final MethodInvocation invocation) throws Throwable {
        if (MethodUtil.isAbstract(invocation.getMethod())) {
            return null;
        }
        return invocation.proceed();
    }

    /**
     * {@link org.seasar.jms.core.MessageSender}を返します。
     * 
     * @return {@link org.seasar.jms.core.MessageSender}
     */
    protected MessageSender getMessageSender() {
        return (MessageSender) componentDef.getComponent();
    }

}
