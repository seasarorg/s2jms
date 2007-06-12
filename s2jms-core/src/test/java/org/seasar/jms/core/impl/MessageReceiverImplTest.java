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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.easymock.IArgumentMatcher;
import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.jms.core.destination.DestinationFactory;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class MessageReceiverImplTest extends S2TigerTestCase {

    MessageReceiverImpl target;

    SessionFactory sf;

    DestinationFactory df;

    Session session;

    Destination destination;

    Topic topic;

    Queue queue;

    MessageConsumer consumer;

    TopicSubscriber subscriber;

    BytesMessage bytesMessage;

    MapMessage mapMessage;

    ObjectMessage objectMessage;

    TextMessage textMessage;

    @SuppressWarnings("unchecked")
    Enumeration enumeration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new MessageReceiverImpl();
        sf = new SessionFactoryImpl();
        df = createStrictMock(DestinationFactory.class);
        session = createStrictMock(Session.class);
        destination = createStrictMock(Destination.class);
        topic = createStrictMock(Topic.class);
        queue = createStrictMock(Queue.class);
        consumer = createStrictMock(MessageConsumer.class);
        subscriber = createStrictMock(TopicSubscriber.class);
        bytesMessage = createStrictMock(BytesMessage.class);
        mapMessage = createStrictMock(MapMessage.class);
        objectMessage = createStrictMock(ObjectMessage.class);
        textMessage = createStrictMock(TextMessage.class);
        enumeration = createStrictMock(Enumeration.class);
    }

    /**
     * @throws Exception
     */
    public void setUpReceiveBytes() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
    }

    /**
     * @throws Exception
     */
    public void testReceiveBytes() throws Exception {
        assertTrue("1", Arrays.equals(new byte[] { 1, 2, 3 }, target.receiveBytes()));
    }

    /**
     * @throws Exception
     */
    public void recordReceiveBytes() throws Exception {
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createConsumer(destination, null, false)).andReturn(consumer);
        expect(consumer.receive()).andReturn(bytesMessage);
        expect(bytesMessage.getBodyLength()).andReturn(3L);
        expect(bytesMessage.readBytes(eqBytes(new byte[] { 1, 2, 3 }))).andReturn(3);
        consumer.close();
    }

    /**
     * @throws Exception
     */
    public void setUpReceiveMap() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
    }

    /**
     * @throws Exception
     */
    public void testReceiveMap() throws Exception {
        Map<String, Object> map = target.receiveMap();
        assertNotNull("1", map);
        assertEquals("2", "FOO", map.get("foo"));
        assertEquals("3", "BAR", map.get("bar"));
    }

    /**
     * @throws Exception
     */
    public void recordReceiveMap() throws Exception {
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createConsumer(destination, null, false)).andReturn(consumer);
        expect(consumer.receive()).andReturn(mapMessage);
        expect(mapMessage.getMapNames()).andReturn(enumeration);
        expect(enumeration.hasMoreElements()).andReturn(true);
        expect(enumeration.nextElement()).andReturn("foo");
        expect(mapMessage.getObject("foo")).andReturn("FOO");
        expect(enumeration.hasMoreElements()).andReturn(true);
        expect(enumeration.nextElement()).andReturn("bar");
        expect(mapMessage.getObject("bar")).andReturn("BAR");
        expect(enumeration.hasMoreElements()).andReturn(false);
        consumer.close();
    }

    /**
     * @throws Exception
     */
    public void setUpReceiveObject() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
    }

    /**
     * @throws Exception
     */
    public void testReceiveObject() throws Exception {
        assertEquals("1", new Integer(-100), target.receiveObject());
    }

    /**
     * @throws Exception
     */
    public void recordReceiveObject() throws Exception {
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createConsumer(destination, null, false)).andReturn(consumer);
        expect(consumer.receive()).andReturn(objectMessage);
        expect(objectMessage.getObject()).andReturn(-100);
        consumer.close();
    }

    /**
     * @throws Exception
     */
    public void setUpReceiveText() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
    }

    /**
     * @throws Exception
     */
    public void testReceiveText() throws Exception {
        assertEquals("1", "HogeHoge", target.receiveText());
    }

    /**
     * @throws Exception
     */
    public void recordReceiveText() throws Exception {
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createConsumer(destination, null, false)).andReturn(consumer);
        expect(consumer.receive()).andReturn(textMessage);
        expect(textMessage.getText()).andReturn("HogeHoge");
        consumer.close();
    }

    /**
     * @throws Exception
     */
    public void setUpReceiveDurable() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
        target.setSubscriptionName("Geho");
    }

    /**
     * @throws Exception
     */
    public void testReceiveDurable() throws Exception {
        assertEquals("1", "HogeHoge", target.receiveText());
    }

    /**
     * @throws Exception
     */
    public void recordReceiveDurable() throws Exception {
        expect(df.getDestination(session)).andReturn(topic);
        expect(session.createDurableSubscriber(topic, "Geho", null, false)).andReturn(subscriber);
        expect(subscriber.receive()).andReturn(textMessage);
        subscriber.close();
        expect(textMessage.getText()).andReturn("HogeHoge");
    }

    /**
     * @throws Exception
     */
    public void setUpDurableNoTopic() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
        target.setSubscriptionName("Geho");
    }

    /**
     * @throws Exception
     */
    public void testDurableNoTopic() throws Exception {
        try {
            target.receiveText();
            fail("1");
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void recordDurableNoTopic() throws Exception {
        expect(df.getDestination(session)).andReturn(destination);
    }

    /**
     * @throws Exception
     */
    public void setUpDurableNoSubscriptionName() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
    }

    /**
     * @throws Exception
     */
    public void testDurableNoSubscriptionName() throws Exception {
        try {
            target.receiveText();
            fail("1");
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void recordDurableNoSubscriptionName() throws Exception {
        expect(df.getDestination(session)).andReturn(topic);
    }

    /**
     * @throws Exception
     */
    public void setUpReceiveNowait() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setTimeout(0);
    }

    /**
     * @throws Exception
     */
    public void testReceiveNowait() throws Exception {
        assertNull(target.receiveText());
    }

    /**
     * @throws Exception
     */
    public void recordReceiveNowait() throws Exception {
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createConsumer(destination, null, false)).andReturn(consumer);
        expect(consumer.receiveNoWait()).andReturn(null);
        consumer.close();
    }

    /**
     * @throws Exception
     */
    public void setUpReceiveSpecificTimeout() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setTimeout(100);
    }

    /**
     * @throws Exception
     */
    public void testReceiveSpecificTimeout() throws Exception {
        assertNull(target.receiveText());
    }

    /**
     * @throws Exception
     */
    public void recordReceiveSpecificTimeout() throws Exception {
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createConsumer(destination, null, false)).andReturn(consumer);
        expect(consumer.receive(100)).andReturn(null);
        consumer.close();
    }

    static byte[] eqBytes(final byte[] expected) {
        reportMatcher(new IArgumentMatcher() {

            public boolean matches(Object arg) {
                byte[] actual = byte[].class.cast(arg);
                if (expected.length != actual.length) {
                    return false;
                }
                System.arraycopy(expected, 0, actual, 0, expected.length);
                return true;
            }

            public void appendTo(StringBuffer buf) {
                buf.append("eqBytes");
            }
        });
        return expected;
    }

    class SessionFactoryImpl implements SessionFactory {

        public void operateSession(boolean startConnection, SessionHandler handler) {
            try {
                assertTrue("startConnection", startConnection);
                handler.handleSession(session);
            } catch (JMSException e) {
                throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
            }
        }

    }

}
