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
package org.seasar.jms.core.message.impl;

import java.util.Arrays;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.seasar.framework.unit.EasyMockTestCase;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;

/**
 * @author koichik
 */
public class AbstractMessageFactoryTest extends EasyMockTestCase {

    MessageFactory target = new MessageFactory();

    @EasyMock(EasyMockType.STRICT)
    Session session;

    @EasyMock(EasyMockType.STRICT)
    Destination destination;

    @EasyMock
    TextMessage message;

    int count = 0;

    /**
     * @throws Exception
     */
    public void testHeader() throws Exception {
        assertNull(target.correlationID);
        assertNull(target.correlationIDAsBytes);
        assertNull(target.replyTo);

        target.setCorrelationID("hoge");
        assertEquals("hoge", target.correlationID);
        assertNull(target.correlationIDAsBytes);

        target.setCorrelationIDAsBytes(new byte[] { 1, 2, 3 });
        assertNull(target.correlationID);
        assertTrue(Arrays.equals(new byte[] { 1, 2, 3 }, target.correlationIDAsBytes));

        target.setReplyTo(destination);
        assertSame(destination, target.replyTo);
    }

    /**
     * @throws Exception
     */
    public void testProperty() throws Exception {
        assertNull(target.properties.get("foo"));
        assertNull(target.properties.get("bar"));
        assertNull(target.properties.get("baz"));

        target.addProperty("foo", "FOO");
        assertEquals("FOO", target.properties.get("foo"));
        assertNull(target.properties.get("bar"));
        assertNull(target.properties.get("baz"));

        target.addProperty("bar", "BAR");
        assertEquals("FOO", target.properties.get("foo"));
        assertEquals("BAR", target.properties.get("bar"));
        assertNull(target.properties.get("baz"));
    }

    /**
     * @throws Exception
     */
    public void testCreateMessage() throws Exception {
        target.setCorrelationID("id");
        target.setReplyTo(destination);
        target.addProperty("foo", "FOO");
        assertSame("1", message, target.createMessage(session));
        assertEquals("2", 1, count);
    }

    /**
     * @throws Exception
     */
    public void recordCreateMessage() throws Exception {
        message.setJMSCorrelationID("id");
        message.setJMSReplyTo(destination);
        message.setObjectProperty("foo", "FOO");
    }

    /**
     */
    public class MessageFactory extends AbstractMessageFactory<TextMessage> {

        @Override
        protected TextMessage createMessageInstance(Session session) throws JMSException {
            return message;
        }

        @Override
        protected void setupPayload(TextMessage message) throws JMSException {
            ++count;
        }
    }

}
