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
package org.seasar.jms.core.message.impl;

import java.util.Enumeration;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class AbstractMessageHandlerTest extends EasyMockTestCase {

    MessageHandler target;

    TextMessage message;

    @SuppressWarnings("unchecked")
    Enumeration enumeration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new MessageHandler();
        message = createStrictMock(TextMessage.class);
        enumeration = createStrictMock(Enumeration.class);
    }

    /**
     * @throws Exception
     */
    public void testGetProperties() throws Exception {
        assertEquals("1", "Test", target.handleMessage(message));
        Map<String, Object> map = target.getProperties();
        assertNotNull("2", map);
        assertEquals("3", 2, map.size());
        assertEquals("4", "FOO", map.get("foo"));
        assertEquals("5", "BAR", map.get("bar"));
    }

    /**
     * @throws Exception
     */
    public void recordGetProperties() throws Exception {
        expect(message.getPropertyNames()).andReturn(enumeration);
        expect(enumeration.hasMoreElements()).andReturn(true);
        expect(enumeration.nextElement()).andReturn("foo");
        expect(message.getObjectProperty("foo")).andReturn("FOO");
        expect(enumeration.hasMoreElements()).andReturn(true);
        expect(enumeration.nextElement()).andReturn("bar");
        expect(message.getObjectProperty("bar")).andReturn("BAR");
        expect(enumeration.hasMoreElements()).andReturn(false);
    }

    /**
     */
    public class MessageHandler extends AbstractMessageHandler<TextMessage, String> {

        @Override
        public String getPayload() throws JMSException {
            return "Test";
        }

        public Class<TextMessage> getMessageType() {
            return TextMessage.class;
        }

        public Class<String> getPayloadType() {
            return String.class;
        }

    }

}
