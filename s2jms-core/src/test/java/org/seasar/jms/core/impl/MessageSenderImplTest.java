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
import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.jms.core.destination.DestinationFactory;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class MessageSenderImplTest extends S2TigerTestCase {

    MessageSenderImpl target;

    SessionFactory sf;

    DestinationFactory df;

    Session session;

    Destination destination;

    MessageProducer producer;

    BytesMessage bytesMessage;

    MapMessage mapMessage;

    ObjectMessage objectMessage;

    TextMessage textMessage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new MessageSenderImpl();
        sf = createStrictMock(SessionFactory.class);
        df = createStrictMock(DestinationFactory.class);
        session = createStrictMock(Session.class);
        destination = createStrictMock(Destination.class);
        producer = createStrictMock(MessageProducer.class);
        bytesMessage = createStrictMock(BytesMessage.class);
        mapMessage = createStrictMock(MapMessage.class);
        objectMessage = createStrictMock(ObjectMessage.class);
        textMessage = createStrictMock(TextMessage.class);
    }

    /**
     * @throws Exception
     */
    public void setUpSendBytes() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
    }

    /**
     * @throws Exception
     */
    public void testSendBytes() throws Exception {
        target.send(new byte[] { 1, 2, 3 });
    }

    /**
     * @throws Exception
     */
    public void recordSendBytes() throws Exception {
        sf.operateSession(eq(false), handler(session));
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createProducer(destination)).andReturn(producer);
        producer.setDisableMessageID(false);
        producer.setDisableMessageTimestamp(false);
        expect(session.createBytesMessage()).andReturn(bytesMessage);
        bytesMessage.writeBytes(aryEq(new byte[] { 1, 2, 3 }));
        producer.send(bytesMessage, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);
        producer.close();
    }

    /**
     * @throws Exception
     */
    public void setUpSendMap() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
    }

    /**
     * @throws Exception
     */
    public void testSendMap() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("foo", "FOO");
        map.put("bar", "BAR");
        target.send(map);
    }

    /**
     * @throws Exception
     */
    public void recordSendMap() throws Exception {
        sf.operateSession(eq(false), handler(session));
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
    }

    /**
     * @throws Exception
     */
    public void setUpSendObject() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
    }

    /**
     * @throws Exception
     */
    public void testSendObject() throws Exception {
        target.send(666);
    }

    /**
     * @throws Exception
     */
    public void recordSendObject() throws Exception {
        sf.operateSession(eq(false), handler(session));
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createProducer(destination)).andReturn(producer);
        producer.setDisableMessageID(false);
        producer.setDisableMessageTimestamp(false);
        expect(session.createObjectMessage()).andReturn(objectMessage);
        objectMessage.setObject(666);
        producer.send(objectMessage, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);
        producer.close();
    }

    /**
     * @throws Exception
     */
    public void setUpSendText() throws Exception {
        target.setSessionFactory(sf);
        target.setDestinationFactory(df);
    }

    /**
     * @throws Exception
     */
    public void testSendText() throws Exception {
        target.send("HogeHoge");
    }

    /**
     * @throws Exception
     */
    public void recordSendText() throws Exception {
        sf.operateSession(eq(false), handler(session));
        expect(df.getDestination(session)).andReturn(destination);
        expect(session.createProducer(destination)).andReturn(producer);
        producer.setDisableMessageID(false);
        producer.setDisableMessageTimestamp(false);
        expect(session.createTextMessage()).andReturn(textMessage);
        textMessage.setText("HogeHoge");
        producer.send(textMessage, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);
        producer.close();
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
