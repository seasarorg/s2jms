/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.io.Serializable;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.seasar.framework.unit.EasyMockTestCase;

import static org.seasar.jms.core.message.impl.MessageHandlerFactory.*;

/**
 * @author koichik
 */
public class MessageHandlerFactoryTest extends EasyMockTestCase {

    /**
     * @throws Exception
     */
    public void testGetMessageHandlerFromMessageType() throws Exception {
        assertTrue(getMessageHandlerFromMessageType(TextMessage.class) instanceof TextMessageHandler);
        assertTrue(getMessageHandlerFromMessageType(MapMessage.class) instanceof MapMessageHandler);
        assertTrue(getMessageHandlerFromMessageType(BytesMessage.class) instanceof BytesMessageHandler);
        assertTrue(getMessageHandlerFromMessageType(ObjectMessage.class) instanceof ObjectMessageHandler);
    }

    /**
     * @throws Exception
     */
    public void testGetMessageHandlerFromPayloadType() throws Exception {
        assertTrue(getMessageHandlerFromPayloadType(String.class) instanceof TextMessageHandler);
        assertTrue(getMessageHandlerFromPayloadType(Map.class) instanceof MapMessageHandler);
        assertTrue(getMessageHandlerFromPayloadType(byte[].class) instanceof BytesMessageHandler);
        assertTrue(getMessageHandlerFromPayloadType(Serializable.class) instanceof ObjectMessageHandler);
    }

}
