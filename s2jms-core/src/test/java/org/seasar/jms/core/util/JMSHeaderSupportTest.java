/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.jms.core.util;

import java.util.Set;

import javax.jms.Destination;
import javax.jms.Message;

import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

;

/**
 * @author koichik
 */
public class JMSHeaderSupportTest extends EasyMockTestCase {

    static final byte[] BYTES = "hoge".getBytes();

    Message message;

    Destination destination;

    Destination replyTo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        message = createStrictMock(Message.class);
        destination = createStrictMock(Destination.class);
        replyTo = createStrictMock(Destination.class);
    }

    /**
     * @throws Exception
     */
    public void testHeaderNames() throws Exception {
        Set<String> headerNames = JMSHeaderSupport.getNames();
        assertEquals(11, headerNames.size());
        assertTrue(headerNames.contains("JMSCorrelationID"));
        assertTrue(headerNames.contains("JMSCorrelationIDAsBytes"));
        assertTrue(headerNames.contains("JMSDeliveryMode"));
        assertTrue(headerNames.contains("JMSDestination"));
        assertTrue(headerNames.contains("JMSExpiration"));
        assertTrue(headerNames.contains("JMSMessageID"));
        assertTrue(headerNames.contains("JMSPriority"));
        assertTrue(headerNames.contains("JMSRedelivered"));
        assertTrue(headerNames.contains("JMSReplyTo"));
        assertTrue(headerNames.contains("JMSTimestamp"));
        assertTrue(headerNames.contains("JMSType"));
        try {
            headerNames.add("hoge");
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testGetValue() throws Exception {
        assertEquals("hoge", JMSHeaderSupport.getValue(message, "JMSCorrelationID"));
        assertEquals(BYTES, JMSHeaderSupport.getValue(message, "JMSCorrelationIDAsBytes"));
        assertEquals(Message.DEFAULT_DELIVERY_MODE, JMSHeaderSupport.getValue(message,
                "JMSDeliveryMode"));
        assertEquals(destination, JMSHeaderSupport.getValue(message, "JMSDestination"));
        assertEquals(Message.DEFAULT_TIME_TO_LIVE, JMSHeaderSupport.getValue(message,
                "JMSExpiration"));
        assertEquals("100", JMSHeaderSupport.getValue(message, "JMSMessageID"));
        assertEquals(Message.DEFAULT_PRIORITY, JMSHeaderSupport.getValue(message, "JMSPriority"));
        assertEquals(true, JMSHeaderSupport.getValue(message, "JMSRedelivered"));
        assertEquals(replyTo, JMSHeaderSupport.getValue(message, "JMSReplyTo"));
        assertEquals(10000L, JMSHeaderSupport.getValue(message, "JMSTimestamp"));
        assertEquals("javax.jms.MapMessage", JMSHeaderSupport.getValue(message, "JMSType"));
    }

    /**
     * @throws Exception
     */
    public void recordGetValue() throws Exception {
        expect(message.getJMSCorrelationID()).andReturn("hoge");
        expect(message.getJMSCorrelationIDAsBytes()).andReturn(BYTES);
        expect(message.getJMSDeliveryMode()).andReturn(Message.DEFAULT_DELIVERY_MODE);
        expect(message.getJMSDestination()).andReturn(destination);
        expect(message.getJMSExpiration()).andReturn(Message.DEFAULT_TIME_TO_LIVE);
        expect(message.getJMSMessageID()).andReturn("100");
        expect(message.getJMSPriority()).andReturn(Message.DEFAULT_PRIORITY);
        expect(message.getJMSRedelivered()).andReturn(true);
        expect(message.getJMSReplyTo()).andReturn(replyTo);
        expect(message.getJMSTimestamp()).andReturn(10000L);
        expect(message.getJMSType()).andReturn("javax.jms.MapMessage");
    }

    /**
     * @throws Exception
     */
    public void testSetValue() throws Exception {
        JMSHeaderSupport.setValue(message, "JMSCorrelationID", "hoge");
        JMSHeaderSupport.setValue(message, "JMSCorrelationIDAsBytes", BYTES);
        JMSHeaderSupport.setValue(message, "JMSDeliveryMode", Message.DEFAULT_DELIVERY_MODE);
        JMSHeaderSupport.setValue(message, "JMSDestination", destination);
        JMSHeaderSupport.setValue(message, "JMSExpiration", Message.DEFAULT_TIME_TO_LIVE);
        JMSHeaderSupport.setValue(message, "JMSMessageID", "100");
        JMSHeaderSupport.setValue(message, "JMSPriority", Message.DEFAULT_PRIORITY);
        JMSHeaderSupport.setValue(message, "JMSRedelivered", true);
        JMSHeaderSupport.setValue(message, "JMSReplyTo", replyTo);
        JMSHeaderSupport.setValue(message, "JMSTimestamp", 10000L);
        JMSHeaderSupport.setValue(message, "JMSType", "javax.jms.MapMessage");
    }

    /**
     * @throws Exception
     */
    public void recordSetValue() throws Exception {
        message.setJMSCorrelationID("hoge");
        message.setJMSCorrelationIDAsBytes(BYTES);
        message.setJMSDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
        message.setJMSDestination(destination);
        message.setJMSExpiration(Message.DEFAULT_TIME_TO_LIVE);
        message.setJMSMessageID("100");
        message.setJMSPriority(Message.DEFAULT_PRIORITY);
        message.setJMSRedelivered(true);
        message.setJMSReplyTo(replyTo);
        message.setJMSTimestamp(10000L);
        message.setJMSType("javax.jms.MapMessage");
    }

}
