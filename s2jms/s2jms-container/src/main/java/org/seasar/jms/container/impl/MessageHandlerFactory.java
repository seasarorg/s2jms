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
package org.seasar.jms.container.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Message;

import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.BytesMessageHandler;
import org.seasar.jms.core.message.impl.MapMessageHandler;
import org.seasar.jms.core.message.impl.ObjectMessageHandler;
import org.seasar.jms.core.message.impl.TextMessageHandler;

/**
 * @author y-komori
 * 
 */
public class MessageHandlerFactory {
    protected static Map<Class<? extends Message>, MessageHandler> handlerMap = new HashMap<Class<? extends Message>, MessageHandler>();
    protected static List<MessageHandler> handlers = new ArrayList<MessageHandler>();
    static {
        handlers.add(new TextMessageHandler());
        handlers.add(new MapMessageHandler());
        handlers.add(new ObjectMessageHandler());
        handlers.add(new BytesMessageHandler());
    }

    private MessageHandlerFactory() {
    }

    protected static MessageHandler getMessageHandler(Class<? extends Message> messageClass) {
        for (MessageHandler messageHandler : handlers) {
            if (messageHandler.getMessageType().isAssignableFrom(messageClass)){
                return messageHandler;
            }
        }
        return null;
    }
}
