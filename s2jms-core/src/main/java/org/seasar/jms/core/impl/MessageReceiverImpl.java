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
package org.seasar.jms.core.impl;

import java.io.Serializable;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.jms.core.MessageReceiver;
import org.seasar.jms.core.destination.DestinationFactory;
import org.seasar.jms.core.exception.SIllegalStateException;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.BytesMessageHandler;
import org.seasar.jms.core.message.impl.MapMessageHandler;
import org.seasar.jms.core.message.impl.ObjectMessageHandler;
import org.seasar.jms.core.message.impl.TextMessageHandler;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

/**
 * JMSメッセージを受信するコンポーネントの実装クラスです。
 * <p>
 * 受信したメッセージを容易に処理するために次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #receiveBytes}</li>
 * <li>{@link #receiveMap}</li>
 * <li>{@link #receiveObject}</li>
 * <li>{@link #receiveText}</li>
 * </ul>
 * これらのメソッドは受信したJMS標準メッセージ型のペイロードを戻り値として返します。
 * </p>
 * <p>
 * 受信したJMSメッセージを独自に処理するには次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #receive(MessageHandler)}</li>
 * </ul>
 * 引数で指定する{@link org.seasar.jms.core.message.MessageHandler}により自由にJMSメッセージを処理することができます。
 * このメソッドは{@link org.seasar.jms.core.message.MessageHandler#handleMessage}の戻り値をそのまま返します。
 * </p>
 * <p>
 * 受信したJMSメッセージをそのまま取得するには次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #receive()}</li>
 * </ul>
 * </p>
 * <p>
 * いずれの場合も{@link #setTimeout timeout}プロパティに設定された時間内にメッセージを受信できなかった場合は{@code null}を返します。
 * 
 * @author koichik
 */
@Component
public class MessageReceiverImpl implements MessageReceiver {

    // instance fields
    /** 受信に使用するJMSセッションのファクトリ */
    protected SessionFactory sessionFactory;

    /** 受信に使用するJMSデスティネーションのファクトリ */
    protected DestinationFactory destinationFactory;

    /** JMSメッセージをデュラブル(継続的)に受信する場合は{@code true} */
    protected boolean durable;

    /** JMSメッセージをデュラブル(継続的)に受信する場合のサブスクリプション名 */
    protected String subscriptionName;

    /** 受信するJMSメッセージを選択するためのメッセージセレクタ */
    protected String messageSelector = null;

    /** 受信するJMSコネクションから送信されたJMSメッセージを受信しない場合は{@code true} */
    protected boolean noLocal = false;

    /** JMSメッセージを受信するまで待機する時間 (ミリ秒単位) */
    protected long timeout = -1;

    /**
     * インスタンスを構築します。
     */
    public MessageReceiverImpl() {
    }

    /**
     * 受信に使用するJMSセッションのファクトリを設定します(必須)。
     * 
     * @param sessionFactory
     *            JMSセッションファクトリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 受信に使用するJMSデスティネーションのファクトリを設定します(必須)。
     * 
     * @param destinationFactory
     *            JMSデスティネーションファクトリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setDestinationFactory(final DestinationFactory destinationFactory) {
        this.destinationFactory = destinationFactory;
    }

    /**
     * JMSメッセージを受信するまで待機する時間をミリ秒単位で設定します。
     * <p>
     * {@code timeout} < 0 の場合はJMSメッセージを受信するまで無制限に待機します。デフォルトです。<br>
     * {@code timeout} > 0 場合はその時間だけ待機します。<br>
     * {@code timeout} == 0 の場合は待機しません。
     * </p>
     * 
     * @param timeout
     *            JMSメッセージを受信するまで待機する時間 (ミリ秒単位)
     */
    @Binding(bindingType = BindingType.MAY)
    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }

    /**
     * 受信するJMSメッセージを選択するためのメッセージセレクタを指定します。 メッセージセレクタの詳細は{@link javax.jms.Message}を参照してください。
     * 
     * @param messageSelector
     *            受信するJMSメッセージを選択するためのメッセージセレクタ
     */
    @Binding(bindingType = BindingType.MAY)
    public void setMessageSelector(final String messageSelector) {
        this.messageSelector = messageSelector;
    }

    /**
     * 受信するJMSコネクションから送信されたJMSメッセージを受信しない場合は{@code true}に設定します。 デフォルトは{@code false}です。
     * JMSデスティネーションがトピックの場合にのみ有効です。
     * 
     * @param noLocal
     *            受信するJMSコネクションから送信されたJMSメッセージを受信しない場合は{@code true}、それ以外の場合は{@code false}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setNoLocal(final boolean noLocal) {
        this.noLocal = noLocal;
    }

    /**
     * JMSメッセージをデュラブル(継続的)に受信する場合は{@code true}に設定します。デフォルトは{@code false}です。
     * JMSデスティネーションがトピックの場合にのみ有効です。
     * 
     * @param durable
     *            JMSメッセージをデュラブル(継続的)に受信する場合は{@code true}、それ以外の場合は{@code false}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDurable(final boolean durable) {
        this.durable = durable;
    }

    /**
     * JMSメッセージをデュラブル(継続的)に受信する場合のサブスクリプション名を設定します。
     * JMSデスティネーションがトピックで、JMSメッセージをデュラブルに受信する場合にのみ有効です。
     * 
     * @param subscriptionName
     *            JMSメッセージをデュラブル(継続的)に受信する場合のサブスクリプション名
     */
    @Binding(bindingType = BindingType.MAY)
    public void setSubscriptionName(final String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public byte[] receiveBytes() {
        return receive(new BytesMessageHandler());
    }

    public String receiveText() {
        return receive(new TextMessageHandler());
    }

    public Serializable receiveObject() {
        return receive(new ObjectMessageHandler());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> receiveMap() {
        return receive(new MapMessageHandler());
    }

    public <MSGTYPE extends Message, T> T receive(final MessageHandler<MSGTYPE, T> messageHandler) {
        final Class<? extends MSGTYPE> clazz = messageHandler.getMessageType();
        final Message message = receive();
        if (message == null) {
            return null;
        }
        return messageHandler.handleMessage(clazz.cast(message));
    }

    public Message receive() {
        final SessionHandlerImpl sessionHandler = new SessionHandlerImpl();
        sessionFactory.operateSession(true, sessionHandler);
        return sessionHandler.getMessage();
    }

    /**
     * プロパティの設定に基づいて{@link javax.jms.MessageConsumer}を作成して返します。
     * 
     * @param session
     *            JMSセッション
     * @return プロパティの設定に基づいて作成した{@link javax.jms.MessageConsumer}
     * @throws javax.jms.IllegalStateException
     *             {@link setDurable durable}プロパティが{@code true}に設定されている場合で、JMSデスティネーションがトピックでないかサブスクリプション名が設定されていない場合にスローされます。
     * @throws javax.jms.JMSException
     *             JMS実装からスローされた例外をそのままスローします
     */
    protected MessageConsumer createMessageConsumer(final Session session) throws JMSException {
        final Destination destination = destinationFactory.getDestination(session);
        if (!durable) {
            return session.createConsumer(destination, messageSelector, noLocal);
        }

        if (!(destination instanceof Topic)) {
            throw new SIllegalStateException("EJMS-CORE1000");
        }
        if (subscriptionName == null) {
            throw new SIllegalStateException("EJMS-CORE1001");
        }
        return session.createDurableSubscriber((Topic) destination, subscriptionName,
                messageSelector, noLocal);
    }

    /**
     * JMSセッションからJMSメッセージを受信するための{@link SesshonHandler}実装クラスです。
     * 
     * @author koichik
     * 
     */
    public class SessionHandlerImpl implements SessionHandler {

        // instance fields
        /** JMSメッセージ */
        protected Message message;

        /**
         * 受信したJMSメッセージを返します。
         * 
         * @return 受信したJMSメッセージ
         */
        public Message getMessage() {
            return message;
        }

        /**
         * {@link MessageReceiverImpl}の{@link #setTimeout timeout}プロパティの設定に基づいてJMSセッションからJMSメッセージを受信し、インスタンスフィールドに保持します。
         * 
         * @param session
         *            JMSセッション
         * @throws javax.jms.JMSException
         *             JMS実装からスローされた例外をそのままスローします
         */
        public void handleSession(final Session session) throws JMSException {
            final MessageConsumer consumer = createMessageConsumer(session);
            try {
                if (timeout > 0) {
                    message = consumer.receive(timeout);
                } else if (timeout == 0) {
                    message = consumer.receiveNoWait();
                } else {
                    message = consumer.receive();
                }
            } finally {
                consumer.close();
            }
        }
    }
}
