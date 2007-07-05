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
package org.seasar.jms.core.impl;

import java.io.Serializable;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.jms.core.MessageSender;
import org.seasar.jms.core.destination.DestinationFactory;
import org.seasar.jms.core.message.MessageFactory;
import org.seasar.jms.core.message.impl.BytesMessageFactory;
import org.seasar.jms.core.message.impl.MapMessageFactory;
import org.seasar.jms.core.message.impl.ObjectMessageFactory;
import org.seasar.jms.core.message.impl.TextMessageFactory;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

/**
 * JMSメッセージを送信するコンポーネントの実装クラスです。
 * <p>
 * 送信するJMSメッセージを容易に作成するために次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #send(byte[])}</li>
 * <li>{@link #send(Map)}</li>
 * <li>{@link #send(Serializable)}</li>
 * <li>{@link #send(String)}</li>
 * </ul>
 * これらのメソッドは対応するJMS標準メッセージ型を作成し、引数をペイロードに設定して送信します。
 * </p>
 * <p>
 * 送信するJMSメッセージを詳細に設定するには次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #send(MessageFactory)}</li>
 * <li>{@link #send()}</li>
 * </ul>
 * 引数またはプロパティに設定する{@link org.seasar.jms.core.message.MessageFactory}により自由にJMSメッセージを作成することができます。
 * </p>
 * 
 * @author koichik
 */
public class MessageSenderImpl implements MessageSender {

    // instance fields

    /** 送信に使用するJMSセッションのファクトリ */
    protected SessionFactory sessionFactory;

    /** 送信に使用するJMSデスティネーションのファクトリ */
    protected DestinationFactory destinationFactory;

    /** 送信するJMSメッセージのファクトリ */
    protected MessageFactory<?> messageFactory;

    /** 送信するJMSメッセージの{@link javax.jms.DeliveryMode 配信モード} */
    protected int deliveryMode = Message.DEFAULT_DELIVERY_MODE;

    /** 送信するJMSメッセージの優先度 */
    protected int priority = Message.DEFAULT_PRIORITY;

    /** 送信するJMSメッセージの生存時間 (ミリ秒単位) */
    protected long timeToLive = Message.DEFAULT_TIME_TO_LIVE;

    /** 送信するJMSメッセージのメッセージIDを無効化する場合に{@code true} */
    protected boolean disableMessageID = false;

    /** 送信するJMSメッセージのタイムスタンプを無効化する場合に{@code true} */
    protected boolean disableMessageTimestamp = false;

    /**
     * インスタンスを構築します。
     */
    public MessageSenderImpl() {
    }

    /**
     * 送信に使用するJMSセッションのファクトリを設定します(必須)。
     * 
     * @param sessionFactory
     *            JMSセッションファクトリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 送信に使用するJMSデスティネーションのファクトリを設定します(必須)。
     * 
     * @param destinationFactory
     *            JMSデスティネーションファクトリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setDestinationFactory(final DestinationFactory destinationFactory) {
        this.destinationFactory = destinationFactory;
    }

    /**
     * 送信するJMSメッセージのファクトリを設定します。
     * 
     * @param messageFactory
     *            JMSメッセージのファクトリ
     */
    @Binding(bindingType = BindingType.MAY)
    public void setMessageFactory(final MessageFactory<?> messageFactory) {
        this.messageFactory = messageFactory;
    }

    /**
     * 送信するJMSメッセージの{@link javax.jms.DeliveryMode 配信モード}を設定します。デフォルトは{@link javax.jms.Message#DEFAULT_DELIVERY_MODE JMSメッセージのデフォルト配信モード}に従います。
     * 
     * @param deliveryMode
     *            送信するJMSメッセージの{@link javax.jms.DeliveryMode 配信モード}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDeliveryMode(final int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    /**
     * 送信するJMSメッセージの優先度を指定します。デフォルトは{@link javax.jms.Message#DEFAULT_PRIORITY JMSメッセージのデフォルト優先度}に従います。
     * 
     * @param priority
     *            送信するJMSメッセージの優先度
     */
    @Binding(bindingType = BindingType.MAY)
    public void setPriority(final int priority) {
        this.priority = priority;
    }

    /**
     * 送信するJMSメッセージの生存時間をミリ秒単位で指定します。デフォルトは{@link javax.jms.Message#DEFAULT_TIME_TO_LIVE JMSメッセージのデフォルト生存時間}に従います。
     * 
     * @param timeToLive
     *            送信するJMSメッセージの生存時間(ミリ秒単位)
     */
    @Binding(bindingType = BindingType.MAY)
    public void setTimeToLive(final long timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * 送信するJMSメッセージのメッセージIDを無効化する場合に{@code true}を設定します。デフォルトは{@code false}です。
     * 
     * @param disableMessageID
     *            送信するJMSメッセージのメッセージIDを無効化する場合は{@code true}、その他の場合は{@code false}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDisableMessageID(final boolean disableMessageID) {
        this.disableMessageID = disableMessageID;
    }

    /**
     * 送信するJMSメッセージのタイムスタンプを無効化する場合に{@code true}を設定します。デフォルトは{@code false}です。
     * 
     * @param disableMessageTimestamp
     *            送信するJMSメッセージのタイムスタンプを無効化する場合は{@code true}、その他の場合は{@code false}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDisableMessageTimestamp(final boolean disableMessageTimestamp) {
        this.disableMessageTimestamp = disableMessageTimestamp;
    }

    public void send(final byte[] bytes) {
        send(new BytesMessageFactory(bytes));
    }

    public void send(final byte[] bytes, final Map<String, Object> properties) {
        send(new BytesMessageFactory(bytes, properties));
    }

    public void send(final String text) {
        send(new TextMessageFactory(text));
    }

    public void send(final String text, final Map<String, Object> properties) {
        send(new TextMessageFactory(text, properties));
    }

    public void send(final Serializable object) {
        send(new ObjectMessageFactory(object));
    }

    public void send(final Serializable object, final Map<String, Object> properties) {
        send(new ObjectMessageFactory(object, properties));
    }

    public void send(final Map<String, Object> map) {
        send(new MapMessageFactory(map));
    }

    public void send(final Map<String, Object> map, final Map<String, Object> properties) {
        send(new MapMessageFactory(map, properties));
    }

    /**
     * プロパティに設定された{@link MessageFactory}が作成したJMSメッセージを送信します。
     */
    public void send() {
        if (messageFactory == null) {
            throw new EmptyRuntimeException("messageFactory");
        }
        send(messageFactory);
    }

    @SuppressWarnings("unchecked")
    public void send(final MessageFactory messageFactory) {
        sessionFactory.operateSession(false, new SessionHandler() {

            public void handleSession(final Session session) throws JMSException {
                final MessageProducer producer = createMessageProducer(session);
                try {
                    final Message message = messageFactory.createMessage(session);
                    producer.send(message, deliveryMode, priority, timeToLive);
                } finally {
                    producer.close();
                }
            }
        });
    }

    /**
     * プロパティの設定に基づいて{@link javax.jms.MessageProducer}を作成して返します。
     * 
     * @param session
     *            JMSセッション
     * @return プロパティの設定に基づいて作成した{@link javax.jms.MessageProducer}
     * @throws JMSException
     *             JMS実装で例外が発生した場合にスローされます
     */
    protected MessageProducer createMessageProducer(final Session session) throws JMSException {
        final Destination destination = destinationFactory.getDestination(session);
        final MessageProducer producer = session.createProducer(destination);
        producer.setDisableMessageID(disableMessageID);
        producer.setDisableMessageTimestamp(disableMessageTimestamp);
        return producer;
    }

}
