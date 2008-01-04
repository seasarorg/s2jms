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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

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
public class MessageSenderImplTest extends S2TigerTestCase {

    MessageSenderImpl target = new MessageSenderImpl();

    @EasyMock(EasyMockType.STRICT)
    SessionFactory sf;

    @EasyMock(EasyMockType.STRICT)
    DestinationFactory df;

    @EasyMock(EasyMockType.STRICT)
    Session session;

    @EasyMock(EasyMockType.STRICT)
    Destination destination;

    @EasyMock(EasyMockType.STRICT)
    MessageProducer producer;

    @EasyMock(EasyMockType.STRICT)
    BytesMessage bytesMessage;

    @EasyMock(EasyMockType.STRICT)
    MapMessage mapMessage;

    @EasyMock(EasyMockType.STRICT)
    ObjectMessage objectMessage;

    @EasyMock(EasyMockType.STRICT)
    TextMessage textMessage;

    /**
     * @throws Exception
     */
    public void testSendBytes() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);

        assertNull(target.getMessage());
        try {
            target.getMessageID();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
        target.send(new byte[] { 1, 2, 3 });
        assertSame(bytesMessage, target.getMessage());
        assertEquals("abcdefg", target.getMessageID());
    }

    /**
     * @throws Exception
     */
    public void recordSendBytes() throws Exception {
        sf.operateSession(handler(session));
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createProducer(destination)).andReturn(producer);
        producer.setDisableMessageID(false);
        producer.setDisableMessageTimestamp(false);
        expect(session.createBytesMessage()).andReturn(bytesMessage);
        bytesMessage.writeBytes(aryEq(new byte[] { 1, 2, 3 }));
        producer.send(bytesMessage, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);
        producer.close();
        expect(bytesMessage.getJMSMessageID()).andReturn("abcdefg");
    }

    /**
     * @throws Exception
     */
    public void testSendMap() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);

        assertNull(target.getMessage());
        try {
            target.getMessageID();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("foo", "FOO");
        map.put("bar", "BAR");
        target.send(map);
        assertSame(mapMessage, target.getMessage());
        assertEquals("abcdefg", target.getMessageID());
    }

    /**
     * @throws Exception
     */
    public void recordSendMap() throws Exception {
        sf.operateSession(handler(session));
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createProducer(destination)).andReturn(producer);
        producer.setDisableMessageID(false);
        producer.setDisableMessageTimestamp(false);
        expect(session.createMapMessage()).andReturn(mapMessage);
        mapMessage.setObject("foo", "FOO");
        mapMessage.setObject("bar", "BAR");
        producer.send(mapMessage, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);
        producer.close();
        expect(mapMessage.getJMSMessageID()).andReturn("abcdefg");
    }

    /**
     * @throws Exception
     */
    public void testSendObject() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);

        assertNull(target.getMessage());
        try {
            target.getMessageID();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
        target.send(666);
        assertSame(objectMessage, target.getMessage());
        assertEquals("abcdefg", target.getMessageID());
    }

    /**
     * @throws Exception
     */
    public void recordSendObject() throws Exception {
        sf.operateSession(handler(session));
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createProducer(destination)).andReturn(producer);
        producer.setDisableMessageID(false);
        producer.setDisableMessageTimestamp(false);
        expect(session.createObjectMessage()).andReturn(objectMessage);
        objectMessage.setObject(666);
        producer.send(objectMessage, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);
        producer.close();
        expect(objectMessage.getJMSMessageID()).andReturn("abcdefg");
    }

    /**
     * @throws Exception
     */
    public void testSendText() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);

        assertNull(target.getMessage());
        try {
            target.getMessageID();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
        target.send("HogeHoge");
        assertSame(textMessage, target.getMessage());
        assertEquals("abcdefg", target.getMessageID());
    }

    /**
     * @throws Exception
     */
    public void recordSendText() throws Exception {
        sf.operateSession(handler(session));
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createProducer(destination)).andReturn(producer);
        producer.setDisableMessageID(false);
        producer.setDisableMessageTimestamp(false);
        expect(session.createTextMessage()).andReturn(textMessage);
        textMessage.setText("HogeHoge");
        producer.send(textMessage, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);
        producer.close();
        expect(textMessage.getJMSMessageID()).andReturn("abcdefg");
    }

    static SessionHandler handler(final Session session) {
        reportMatcher(new IArgumentMatcher() {

            public boolean matches(Object arg) {
                try {
                    SessionHandler handler = SessionHandler.class.cast(arg);
                    handler.handleSession(session);
                    return true;
                } catch (JMSException e) {
                    throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
                }
            }

            public void appendTo(StringBuffer buf) {
                buf.append("SessionHandler");
            }
        });
        return null;
    }

}
