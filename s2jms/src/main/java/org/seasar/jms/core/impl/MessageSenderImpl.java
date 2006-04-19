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
 * @author koichik
 */
public class MessageSenderImpl implements MessageSender {
    protected SessionFactory sessionFactory;
    protected DestinationFactory destinationFactory;
    protected MessageFactory messageFactory;
    protected int deliveryMode = Message.DEFAULT_DELIVERY_MODE;
    protected int priority = Message.DEFAULT_PRIORITY;
    protected long timeToLive = Message.DEFAULT_TIME_TO_LIVE;
    protected boolean disableMessageID = false;
    protected boolean disableMessageTimestamp = false;

    public MessageSenderImpl() {
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setDestinationFactory(final DestinationFactory destinationFactory) {
        this.destinationFactory = destinationFactory;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setMessageFactory(final MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setDeliveryMode(final int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setPriority(final int priority) {
        this.priority = priority;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setTimeToLive(final long timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setDisableMessageID(final boolean disableMessageID) {
        this.disableMessageID = disableMessageID;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setDisableMessageTimestamp(final boolean disableMessageTimestamp) {
        this.disableMessageTimestamp = disableMessageTimestamp;
    }

    public void send() {
        if (messageFactory == null) {
            throw new EmptyRuntimeException("messageFactory");
        }
        send(messageFactory);
    }

    public void send(final byte[] bytes) {
        send(new BytesMessageFactory(bytes));
    }

    public void send(final String text) {
        send(new TextMessageFactory(text));
    }

    public void send(final Serializable object) {
        send(new ObjectMessageFactory(object));
    }

    public void send(final Map<String, Object> map) {
        send(new MapMessageFactory(map));
    }

    public void send(final MessageFactory messageFactory) {
        sessionFactory.operateSession(false, new SessionHandler() {
            public void handleSession(Session session) throws JMSException {
                final MessageProducer producer = createMessageProducer(session);
                final Message message = messageFactory.createMessage(session);
                producer.send(message, deliveryMode, priority, timeToLive);
            }
        });
    }

    protected MessageProducer createMessageProducer(final Session session) throws JMSException {
        final Destination destination = destinationFactory.getDestination(session);
        final MessageProducer producer = session.createProducer(destination);
        producer.setDisableMessageID(disableMessageID);
        producer.setDisableMessageTimestamp(disableMessageTimestamp);
        return producer;
    }
}
