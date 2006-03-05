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

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import org.seasar.jms.container.exception.NotSupportedMessageRuntimeException;
import org.seasar.jms.core.message.impl.BytesMessageHandler;

/**
 * @author Kenichiro Murata
 * 
 */
public class BytesMessageBinder extends AnnotationMessageBinder {
    private BytesMessageHandler messageHandler = new BytesMessageHandler();

    @Override
    protected Object getPayload(final Message message) throws JMSException {
        if (!(message instanceof BytesMessage)) {
            throw new NotSupportedMessageRuntimeException(message);
        }
        return messageHandler.handleMessage((BytesMessage)message);
    }

    public Class<? extends Message> getMessageType() {
        return BytesMessage.class;
    }
}
