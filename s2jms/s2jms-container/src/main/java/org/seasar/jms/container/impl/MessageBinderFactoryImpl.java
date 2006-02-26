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
import java.util.List;

import javax.jms.Message;

import org.seasar.jms.container.MessageBinder;
import org.seasar.jms.container.MessageBinderFactory;

/**
 * @author y-komori
 * 
 */
public class MessageBinderFactoryImpl implements MessageBinderFactory {
    private final List<MessageBinder> messageBinderList = new ArrayList<MessageBinder>();
    private int messageBinderNum;

    public MessageBinderFactoryImpl() {
        addMessageBinder(new MapMessageBinder());
        addMessageBinder(new TextMessageBinder());
        addMessageBinder(new ObjectMessageBinder());
        addMessageBinder(new BytesMessageBinder());
    }

    public void addMessageBinder(MessageBinder messageBinder) {
        messageBinderList.add(messageBinder);
        messageBinderNum = messageBinderList.size();
    }

    public MessageBinder getMessageBinder(Message message) {
        MessageBinder retMessageBinder = null;
        
        for (int index = 0; index < messageBinderNum; index++) {
            MessageBinder messageBinder = messageBinderList.get(index);
            
            Class<? extends Message> messageClass = messageBinder.getMessageType();
            if (messageClass.isAssignableFrom(message.getClass())) {
                retMessageBinder = messageBinder;
            }
        }
        return retMessageBinder;
    }
}
