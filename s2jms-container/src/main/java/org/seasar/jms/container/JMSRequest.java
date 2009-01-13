/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.jms.container;

import javax.jms.Message;

/**
 * S2JMS-ContainerがSeasar2における外部コンテキストのリクエストとして提供するオブジェクトのインタフェースです。
 * <p>
 * S2JMS-ContainerはJMSの{@link javax.jms.Message}をリクエストオブジェクトとしてアクセス可能にします。
 * </p>
 * 
 * @author y-komori
 * @see org.seasar.framework.container.ExternalContext
 */
public interface JMSRequest {

    /**
     * 受信したJMSメッセージを返します。
     * 
     * @return 受信したJMSメッセージ
     */
    public Message getMessage();

    /**
     * 指定された名前に一致する属性の値を返します。
     * 
     * @param name
     *            属性の名前
     * @return 属性の値
     */
    public Object getAttribute(String name);

    /**
     * 属性の値を指定された名前で設定します。
     * 
     * @param name
     *            属性の名前
     * @param value
     *            属性の値
     */
    public void setAttribute(String name, Object value);

}
