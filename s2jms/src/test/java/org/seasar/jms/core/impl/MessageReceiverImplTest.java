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
import org.seasar.framework.unit.EasyMockTestCase;
import org.seasar.jms.core.destination.DestinationFactory;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.reportMatcher;

/**
 * @author koichik
 */
public class MessageReceiverImplTest extends EasyMockTestCase {
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
    Enumeration enumeration;

    public MessageReceiverImplTest() {
    }

    public MessageReceiverImplTest(String name) {
        super(name);
    }

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

    public void testReceiveBytes() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertTrue("1", Arrays.equals(new byte[] { 1, 2, 3 }, target.receiveBytes()));
            }

            @Override
            public void record() throws Exception {
                expect(df.getDestination(session)).andReturn(destination);
                expect(session.createConsumer(destination, null, false)).andReturn(consumer);
                expect(consumer.receive()).andReturn(bytesMessage);
                expect(bytesMessage.getBodyLength()).andReturn(3L);
                expect(bytesMessage.readBytes(eqBytes(new byte[] { 1, 2, 3 }))).andReturn(3);
            }
        }.doTest();
    }

    public void testReceiveMap() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Map<String, Object> map = target.receiveMap();
                assertNotNull("1", map);
                assertEquals("2", "FOO", map.get("foo"));
                assertEquals("3", "BAR", map.get("bar"));
            }

            @Override
            public void record() throws Exception {
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
            }
        }.doTest();
    }

    public void testReceiveObject() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals("1", new Integer(-100), target.receiveObject());
            }

            @Override
            public void record() throws Exception {
                expect(df.getDestination(session)).andReturn(destination);
                expect(session.createConsumer(destination, null, false)).andReturn(consumer);
                expect(consumer.receive()).andReturn(objectMessage);
                expect(objectMessage.getObject()).andReturn(-100);
            }
        }.doTest();
    }

    public void testReceiveText() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals("1", "HogeHoge", target.receiveText());
            }

            @Override
            public void record() throws Exception {
                expect(df.getDestination(session)).andReturn(destination);
                expect(session.createConsumer(destination, null, false)).andReturn(consumer);
                expect(consumer.receive()).andReturn(textMessage);
                expect(textMessage.getText()).andReturn("HogeHoge");
            }
        }.doTest();
    }

    public void testReceiveDurable() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
        target.setSubscriptionName("Geho");
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals("1", "HogeHoge", target.receiveText());
            }

            @Override
            public void record() throws Exception {
                expect(df.getDestination(session)).andReturn(topic);
                expect(session.createDurableSubscriber(topic, "Geho", null, false)).andReturn(
                        subscriber);
                expect(subscriber.receive()).andReturn(textMessage);
                expect(textMessage.getText()).andReturn("HogeHoge");
            }
        }.doTest();
    }

    public void testDurableNoTopic() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
        target.setSubscriptionName("Geho");
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                try {
                    target.receiveText();
                    fail("1");
                } catch (Exception expected) {
                }
            }

            @Override
            public void record() throws Exception {
                expect(df.getDestination(session)).andReturn(destination);
            }
        }.doTest();
    }

    public void testDurableNoSubscriptionName() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setDurable(true);
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                try {
                    target.receiveText();
                    fail("1");
                } catch (Exception expected) {
                }
            }

            @Override
            public void record() throws Exception {
                expect(df.getDestination(session)).andReturn(topic);
            }
        }.doTest();
    }

    public void testReceiveNowait() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setTimeout(0);
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals("1", null, target.receiveText());
            }

            @Override
            public void record() throws Exception {
                expect(df.getDestination(session)).andReturn(destination);
                expect(session.createConsumer(destination, null, false)).andReturn(consumer);
                expect(consumer.receiveNoWait()).andReturn(null);
            }
        }.doTest();
    }

    public void testReceiveSpecificTimeout() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
        target.setTimeout(100);
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals("1", null, target.receiveText());
            }

            @Override
            public void record() throws Exception {
                expect(df.getDestination(session)).andReturn(destination);
                expect(session.createConsumer(destination, null, false)).andReturn(consumer);
                expect(consumer.receive(100)).andReturn(null);
            }
        }.doTest();
    }

    public class SessionFactoryImpl implements SessionFactory {
        public void operateSession(boolean startConnection, SessionHandler handler) {
            try {
                assertTrue("startConnection", startConnection);
                handler.handleSession(session);
            } catch (JMSException e) {
                throw new SJMSRuntimeException("EJMS0000", e);
            }
        }
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
}
