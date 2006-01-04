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
import javax.jms.QueueBrowser;
import javax.jms.TemporaryQueue;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.seasar.jms.core.exception.SJMSException;

/**
 * @author koichik
 */
public class TopicSessionHandleImpl extends SessionHandleImpl implements TopicSession {
    public TopicSessionHandleImpl() {
    }

    protected TopicSession getPhysicalTopicSession() {
        return (TopicSession) ps;
    }

    public TopicPublisher createPublisher(final Topic topic) throws JMSException {
        return getPhysicalTopicSession().createPublisher(topic);
    }

    public TopicSubscriber createSubscriber(final Topic topic) throws JMSException {
        return getPhysicalTopicSession().createSubscriber(topic);
    }

    public TopicSubscriber createSubscriber(final Topic topic, final String messageSelector,
            final boolean noLocal) throws JMSException {
        return getPhysicalTopicSession().createSubscriber(topic, messageSelector, noLocal);
    }

    @Override
    public MessageProducer createProducer(final Destination destination) throws JMSException {
        return createPublisher((Topic) destination);
    }

    @Override
    public MessageConsumer createConsumer(final Destination destination) throws JMSException {
        return createSubscriber((Topic) destination);
    }

    @Override
    public MessageConsumer createConsumer(final Destination destination,
            final String messageSelector) throws JMSException {
        return createSubscriber((Topic) destination, messageSelector, false);
    }

    @Override
    public MessageConsumer createConsumer(final Destination destination,
            final String messageSelector, final boolean noLocal) throws JMSException {
        return createSubscriber((Topic) destination, messageSelector, noLocal);
    }

    @Override
    public int getAcknowledgeMode() throws JMSException {
        return ch.getAcknowledgeMode();
    }

    @Override
    public QueueBrowser createBrowser(final Queue queue) throws JMSException {
        throw new SJMSException("");
    }

    @Override
    public QueueBrowser createBrowser(final Queue queue, final String messageSelector)
            throws JMSException {
        throw new SJMSException("");
    }

    @Override
    public Queue createQueue(final String queueName) throws JMSException {
        throw new SJMSException("");
    }

    @Override
    public TemporaryQueue createTemporaryQueue() throws JMSException {
        throw new SJMSException("");
    }
}
