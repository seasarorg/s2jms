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
package org.seasar.jms.core.container.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.seasar.jms.core.container.MessageBinder;
import org.seasar.jms.core.container.MessageBinderFactory;

/**
 * @author y-komori
 * 
 */
public class MessageBinderFactoryImpl implements MessageBinderFactory {
    private final Map<Class, MessageBinder> messageBinderMap = new HashMap<Class, MessageBinder>();

    public MessageBinderFactoryImpl() {
        addMessageBinder(MapMessage.class, new MapMessageBinder());
        addMessageBinder(TextMessage.class, new TextMessageBinder());
        addMessageBinder(ObjectMessage.class, new ObjectMessageBinder());
        addMessageBinder(BytesMessage.class, new BytesMessageBinder());
    }

    public void addMessageBinder(Class clazz, MessageBinder messageBinder) {
        if ((clazz != null) && (messageBinder != null)) {
            messageBinderMap.put(clazz, messageBinder);
        }
    }

    public MessageBinder getMessageBinder(Message message) {
        MessageBinder messageBinder = null;
        Class[] interfaces = message.getClass().getInterfaces();
        for (Class msgInterface : interfaces) {
            messageBinder = messageBinderMap.get(msgInterface);
            if (messageBinder != null)
            {
                break;
            }
        }
        return messageBinder;
    }
}
