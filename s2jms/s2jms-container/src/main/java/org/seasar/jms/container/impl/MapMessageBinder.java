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
import javax.jms.MapMessage;
import javax.jms.Message;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.jms.container.exception.NotSupportedMessageRuntimeException;
import org.seasar.jms.core.message.impl.MapMessageHandler;

/**
 * @author y-komori
 * 
 */
public class MapMessageBinder extends AnnotationMessageBinder {
    private MapMessageHandler messageHandler = new MapMessageHandler();

    @Override
    protected boolean bindPayload(final PropertyDesc pd, final Object target, final String propertyName,
            Message message) throws JMSException {
        if(super.bindPayload(pd, target, propertyName, message)){
            return true;
        }

        if (!(message instanceof MapMessage)) {
            throw new NotSupportedMessageRuntimeException(message);
        }
        
        MapMessage mapMessage = (MapMessage) message;
        if (mapMessage.itemExists(propertyName)) {
            setValue(pd, target, mapMessage.getObject(propertyName));
            return true;
        }
        return false;
    }

    @Override
    protected Object getPayload(final Message message) throws JMSException {
        if (!(message instanceof MapMessage)) {
            throw new NotSupportedMessageRuntimeException(message);
        }
        return messageHandler.handleMessage((MapMessage)message);
    }

    public Class<? extends Message> getMessageType() {
        return MapMessage.class;
    }
}
