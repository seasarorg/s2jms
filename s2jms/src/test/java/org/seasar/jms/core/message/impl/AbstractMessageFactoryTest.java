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
package org.seasar.jms.core.message.impl;

import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.seasar.jca.unit.EasyMockTestCase;

/**
 * @author koichik
 */
public class AbstractMessageFactoryTest extends EasyMockTestCase {
    MessageFactory target;
    Session session;
    TextMessage message;
    int count;

    public AbstractMessageFactoryTest() {
    }

    public AbstractMessageFactoryTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new MessageFactory();
        session = createStrictMock(Session.class);
        message = createStrictMock(TextMessage.class);
        count = 0;
    }

    public void testCorrelationID() throws Exception {
        assertNull("1", target.getCorrelationID());
        assertNull("2", target.getCorrelationIDAsBytes());

        target.setCorrelationID("hoge");
        assertEquals("3", "hoge", target.getCorrelationID());
        assertNull("4", target.getCorrelationIDAsBytes());

        target.setCorrelationIDAsBytes(new byte[] { 1, 2, 3 });
        assertNull("3", target.getCorrelationID());
        assertTrue("4", Arrays.equals(new byte[] { 1, 2, 3 }, target.correlationIDAsBytes));
    }

    public void testProperty() throws Exception {
        assertNull("1", target.getProperty("foo"));
        assertNull("2", target.getProperty("bar"));
        assertNull("3", target.getProperty("baz"));

        target.addProperty("foo", "FOO");
        assertEquals("4", "FOO", target.getProperty("foo"));
        assertNull("5", target.getProperty("bar"));
        assertNull("6", target.getProperty("baz"));

        target.addProperty("bar", "BAR");
        assertEquals("7", "FOO", target.getProperty("foo"));
        assertEquals("8", "BAR", target.getProperty("bar"));
        assertNull("9", target.getProperty("baz"));
    }

    public void testCreateMessage() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                target.setCorrelationID("id");
                target.addProperty("foo", "FOO");
                assertSame("1", message, target.createMessage(session));
                assertEquals("2", 1, count);
            }

            @Override
            public void verify() throws Exception {
                message.setJMSCorrelationID("id");
                message.setObjectProperty("foo", "FOO");
            }
        }.doTest();
    }

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
