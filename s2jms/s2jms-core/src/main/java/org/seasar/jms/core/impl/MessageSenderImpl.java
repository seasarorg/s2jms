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
import org.seasar.jms.core.MessageSender;
import org.seasar.jms.core.message.MessageFactory;
import org.seasar.jms.core.message.impl.BytesMessageFactory;
import org.seasar.jms.core.message.impl.MapMessageFactory;
import org.seasar.jms.core.message.impl.ObjectMessageFactory;
import org.seasar.jms.core.message.impl.TextMessageFactory;

/**
 * @author koichik
 */
public class MessageSenderImpl extends AbstractMessageProcessor<Object, MessageFactory> implements
        MessageSender {
    protected int deliveryMode = Message.DEFAULT_DELIVERY_MODE;
    protected int priority = Message.DEFAULT_PRIORITY;
    protected long timeToLive = Message.DEFAULT_TIME_TO_LIVE;
    protected boolean disableMessageID = false;
    protected boolean disableMessageTimestamp = false;

    public MessageSenderImpl() {
        super(false);
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setDeliveryMode(final int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public int getPriority() {
        return priority;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setTimeToLive(final long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public boolean isDisableMessageID() {
        return disableMessageID;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setDisableMessageID(final boolean disableMessageID) {
        this.disableMessageID = disableMessageID;
    }

    public boolean isDisableMessageTimestamp() {
        return disableMessageTimestamp;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setDisableMessageTimestamp(final boolean disableMessageTimestamp) {
        this.disableMessageTimestamp = disableMessageTimestamp;
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
        process(messageFactory);
    }

    @Override
    protected Object processSession(final Session session, final MessageFactory messageFactory)
            throws JMSException {
        final MessageProducer producer = createMessageProducer(session);
        final Message message = messageFactory.createMessage(session);
        producer.send(message, deliveryMode, priority, timeToLive);
        return null;
    }

    protected MessageProducer createMessageProducer(final Session session) throws JMSException {
        final Destination destination = getDestination(session);
        final MessageProducer producer = session.createProducer(destination);
        producer.setDisableMessageID(disableMessageID);
        producer.setDisableMessageTimestamp(disableMessageTimestamp);
        return producer;
    }
}
