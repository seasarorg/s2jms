/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.jms.core.destination.DestinationFactory;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class MessageReceiverImplTest extends S2TigerTestCase {

    MessageReceiverImpl target = new MessageReceiverImpl();;

    SessionFactory sf = new SessionFactoryImpl();

    @EasyMock(EasyMockType.STRICT)
    DestinationFactory df;

    @EasyMock(EasyMockType.STRICT)
    Session session;

    @EasyMock(EasyMockType.STRICT)
    Destination destination;

    @EasyMock(EasyMockType.STRICT)
    Topic topic;

    @EasyMock(EasyMockType.STRICT)
    Queue queue;

    @EasyMock(EasyMockType.STRICT)
    MessageConsumer consumer;

    @EasyMock(EasyMockType.STRICT)
    TopicSubscriber subscriber;

    @EasyMock(EasyMockType.STRICT)
    BytesMessage bytesMessage;

    @EasyMock(EasyMockType.STRICT)
    MapMessage mapMessage;

    @EasyMock(EasyMockType.STRICT)
    ObjectMessage objectMessage;

    @EasyMock(EasyMockType.STRICT)
    TextMessage textMessage;

    @EasyMock(EasyMockType.STRICT)
    @SuppressWarnings("unchecked")
    Enumeration enumeration;

    /**
     * @throws Exception
     */
    public void testReceiveBytes() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);

        assertNull(target.getMessage());
        try {
            target.getMessageID();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
        assertTrue("1", Arrays.equals(new byte[] { 1, 2, 3 }, target.receiveBytes()));
        assertSame(bytesMessage, target.getMessage());
        assertEquals("abcdefg", target.getMessageID());
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
        expect(bytesMessage.getJMSMessageID()).andReturn("abcdefg");
    }

    /**
     * @throws Exception
     */
    public void testReceiveMap() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);

        assertNull(target.getMessage());
        try {
            target.getMessageID();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
        Map<String, Object> map = target.receiveMap();
        assertNotNull("1", map);
        assertEquals("2", "FOO", map.get("foo"));
        assertEquals("3", "BAR", map.get("bar"));
        assertSame(mapMessage, target.getMessage());
        assertEquals("abcdefg", target.getMessageID());
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
        expect(mapMessage.getJMSMessageID()).andReturn("abcdefg");
    }

    /**
     * @throws Exception
     */
    public void testReceiveObject() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);

        assertNull(target.getMessage());
        try {
            target.getMessageID();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
        assertEquals("1", new Integer(-100), target.receiveObject());
        assertSame(objectMessage, target.getMessage());
        assertEquals("abcdefg", target.getMessageID());
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
        expect(objectMessage.getJMSMessageID()).andReturn("abcdefg");
    }

    /**
     * @throws Exception
     */
    public void testReceiveText() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);

        assertNull(target.getMessage());
        try {
            target.getMessageID();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
        assertEquals("1", "HogeHoge", target.receiveText());
        assertSame(textMessage, target.getMessage());
        assertEquals("abcdefg", target.getMessageID());
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
        expect(textMessage.getJMSMessageID()).andReturn("abcdefg");
    }

    /**
     * @throws Exception
     */
    public void testReceiveDurable() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
        target.setSubscriptionName("Geho");
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
    public void testDurableNoTopic() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
        target.setSubscriptionName("Geho");
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
    public void testDurableNoSubscriptionName() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
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
    public void testReceiveNowait() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setTimeout(0);
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
    public void testReceiveSpecificTimeout() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setTimeout(100);
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

        public void operateSession(SessionHandler handler) {
            try {
                handler.handleSession(session);
            } catch (JMSException e) {
                throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
            }
        }

    }

}
