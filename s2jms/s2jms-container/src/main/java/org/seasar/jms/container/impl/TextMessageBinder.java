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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * @author y-komori
 * 
 */
public class TextMessageBinder extends AnnotationMessageBinder {
    @Override
    protected Object getPayload(Message message) throws JMSException {
        Object payload = null;
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            payload = textMessage.getText();
        }
        return payload;
    }
}
