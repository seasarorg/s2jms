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
package org.seasar.jms.core.destination.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * JMSメッセージの{@link javax.jms.Message#setJMSReplyTo JMSReplyTo}ヘッダに設定されている
 * JMSデスティネーションを取得するコンポーネントです。
 * <p>
 * このコンポーネントはJMSメッセージからデスティネーションを作成するため、JMSメッセージ毎に異なったインスタンスを生成する必要があります。
 * 通常はJMSメッセージをインスタンスモードPROTOTYPEまたはREQUESTでS2コンテナに登録し、このコンポーネントも同じか
 * よりライフサイクルの短いインスタンスモードで登録してください。
 * </p>
 * 
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class ReplyToDestinationFactory extends AbstractDestinationFactory {

    // instance fields
    /** 受信JMSメッセージ */
    protected Message message;

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setMessage message}プロパティの設定は必須となります。
     * </p>
     * 
     */
    public ReplyToDestinationFactory() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param message
     *            JMSメッセージ
     */
    public ReplyToDestinationFactory(final Message message) {
        this.message = message;
    }

    /**
     * JMSメッセージを返します。
     * 
     * @return JMSメッセージ
     */
    public Message getMessage() {
        return message;
    }

    /**
     * JMSメッセージを設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティの設定は必須です。
     * </p>
     * 
     * @param message
     *            JMSメッセージ
     */
    @Binding(bindingType = BindingType.MAY)
    public void setMessage(final Message message) {
        this.message = message;
    }

    /**
     * {@link #setMessage message}プロパティに設定されたJMSメッセージの
     * {@link javax.jms.Message#setJMSReplyTo JMSReplyTo}ヘッダから
     * JMSデスティネーションを作成して返します。
     * <p>
     * このメソッドは{@link org.seasar.jms.core.destination.impl.AbstractDestinationFactory#getDestination}が
     * 最初に呼び出された時に一度だけ呼び出されます。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSデスティネーション
     */
    @Override
    protected Destination createDestination(final Session session) throws JMSException {
        return message.getJMSReplyTo();
    }
}
