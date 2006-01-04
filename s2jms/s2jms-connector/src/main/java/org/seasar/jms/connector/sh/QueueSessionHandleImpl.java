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
package org.seasar.jms.connector.sh;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.seasar.jms.core.exception.SJMSException;

/**
 * @author koichik
 */
public class QueueSessionHandleImpl extends SessionHandleImpl implements QueueSession {
    public QueueSessionHandleImpl() {
    }

    protected QueueSession getPhysicalQueueSession() {
        return (QueueSession) ps;
    }

    public QueueSender createSender(final Queue queue) throws JMSException {
        return getPhysicalQueueSession().createSender(queue);
    }

    public QueueReceiver createReceiver(final Queue queue) throws JMSException {
        return getPhysicalQueueSession().createReceiver(queue);
    }

    public QueueReceiver createReceiver(final Queue queue, final String messageSelector)
            throws JMSException {
        return getPhysicalQueueSession().createReceiver(queue, messageSelector);
    }

    @Override
    public MessageProducer createProducer(final Destination destination) throws JMSException {
        return createSender((Queue) destination);
    }

    @Override
    public MessageConsumer createConsumer(final Destination destination) throws JMSException {
        return createReceiver((Queue) destination);
    }

    @Override
    public MessageConsumer createConsumer(final Destination destination,
            final String messageSelector) throws JMSException {
        return createReceiver((Queue) destination, messageSelector);
    }

    @Override
    public MessageConsumer createConsumer(final Destination destination, String messageSelector,
            boolean noLocal) throws JMSException {
        return createReceiver((Queue) destination, messageSelector);
    }

    @Override
    public int getAcknowledgeMode() throws JMSException {
        return ch.getAcknowledgeMode();
    }

    @Override
    public TopicSubscriber createDurableSubscriber(final Topic topic, final String name)
            throws JMSException {
        throw new SJMSException("");
    }

    @Override
    public TopicSubscriber createDurableSubscriber(final Topic topic, final String name,
            final String messageSelector, final boolean noLocal) throws JMSException {
        throw new SJMSException("");
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        throw new SJMSException("");
    }

    @Override
    public Topic createTopic(final String topicName) throws JMSException {
        throw new SJMSException("");
    }

    @Override
    public void unsubscribe(String name) throws JMSException {
        throw new SJMSException("");
    }
}
