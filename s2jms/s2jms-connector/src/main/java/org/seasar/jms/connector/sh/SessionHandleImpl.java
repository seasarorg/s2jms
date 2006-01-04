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

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.jms.connector.ConnectionHandle;
import org.seasar.jms.connector.SessionHandle;
import org.seasar.jms.core.exception.SJMSException;

/**
 * @author koichik
 */
public class SessionHandleImpl implements Session, SessionHandle {
    protected ConnectionHandle ch;
    protected Session ps;

    public SessionHandleImpl() {
    }

    public void associateSession(final ConnectionHandle ch, final Session ps) throws JMSException {
        this.ch = ch;
        this.ps = ps;
    }

    public void cleanup() {
        ch = null;
        ps = null;
    }

    protected void assertOpened() throws JMSException {
        if (ps == null) {
            throw new SJMSException("");
        }
    }

    public void close() throws JMSException {
        if (ch != null) {
            ch.sessionClosed(this);
        }
    }

    public void commit() throws JMSException {
        assertOpened();
        ps.commit();
    }

    public void rollback() throws JMSException {
        assertOpened();
        ps.rollback();
    }

    public void run() {
        if (ps == null) {
            throw new SIllegalStateException("", null);
        }
        ps.run();
    }

    public void recover() throws JMSException {
        assertOpened();
        ps.recover();
    }

    public MessageListener getMessageListener() throws JMSException {
        assertOpened();
        return ps.getMessageListener();
    }

    public void setMessageListener(final MessageListener listener) throws JMSException {
        assertOpened();
        ps.setMessageListener(listener);
    }

    public QueueBrowser createBrowser(final Queue queue) throws JMSException {
        assertOpened();
        return ps.createBrowser(queue);
    }

    public QueueBrowser createBrowser(final Queue queue, final String messageSelector)
            throws JMSException {
        assertOpened();
        return ps.createBrowser(queue, messageSelector);
    }

    public BytesMessage createBytesMessage() throws JMSException {
        assertOpened();
        return ps.createBytesMessage();
    }

    public MessageConsumer createConsumer(final Destination destination) throws JMSException {
        assertOpened();
        return ps.createConsumer(destination);
    }

    public MessageConsumer createConsumer(final Destination destination,
            final String messageSelector) throws JMSException {
        assertOpened();
        return ps.createConsumer(destination, messageSelector);
    }

    public MessageConsumer createConsumer(final Destination destination,
            final String messageSelector, final boolean noLocal) throws JMSException {
        assertOpened();
        return ps.createConsumer(destination, messageSelector, noLocal);
    }

    public TopicSubscriber createDurableSubscriber(final Topic topic, final String name)
            throws JMSException {
        assertOpened();
        return ps.createDurableSubscriber(topic, name);
    }

    public TopicSubscriber createDurableSubscriber(final Topic topic, final String name,
            final String messageSelector, final boolean noLocal) throws JMSException {
        assertOpened();
        return ps.createDurableSubscriber(topic, name, messageSelector, noLocal);
    }

    public MapMessage createMapMessage() throws JMSException {
        assertOpened();
        return ps.createMapMessage();
    }

    public Message createMessage() throws JMSException {
        assertOpened();
        return ps.createMessage();
    }

    public ObjectMessage createObjectMessage() throws JMSException {
        assertOpened();
        return ps.createObjectMessage();
    }

    public ObjectMessage createObjectMessage(final Serializable object) throws JMSException {
        assertOpened();
        return ps.createObjectMessage(object);
    }

    public MessageProducer createProducer(final Destination destination) throws JMSException {
        assertOpened();
        return ps.createProducer(destination);
    }

    public Queue createQueue(final String queueName) throws JMSException {
        assertOpened();
        return ps.createQueue(queueName);
    }

    public StreamMessage createStreamMessage() throws JMSException {
        assertOpened();
        return ps.createStreamMessage();
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        assertOpened();
        return ps.createTemporaryQueue();
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException {
        assertOpened();
        return ps.createTemporaryTopic();
    }

    public TextMessage createTextMessage() throws JMSException {
        assertOpened();
        return ps.createTextMessage();
    }

    public TextMessage createTextMessage(final String text) throws JMSException {
        assertOpened();
        return ps.createTextMessage(text);
    }

    public Topic createTopic(final String topicName) throws JMSException {
        assertOpened();
        return ps.createTopic(topicName);
    }

    public int getAcknowledgeMode() throws JMSException {
        assertOpened();
        return ps.getAcknowledgeMode();
    }

    public boolean getTransacted() throws JMSException {
        assertOpened();
        return ps.getTransacted();
    }

    public void unsubscribe(final String name) throws JMSException {
        assertOpened();
        ps.unsubscribe(name);
    }
}
