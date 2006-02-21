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

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.jms.container.exception.NotSupportedMessageRuntimeException;
import org.seasar.jms.core.util.IterableAdapter;

/**
 * @author y-komori
 * 
 */
public class MapMessageBinder extends AnnotationMessageBinder {

    @Override
    protected boolean bindPayload(PropertyDesc pd, Object target, String propertyName,
            Message message) throws JMSException {
        boolean hasBound = super.bindPayload(pd, target, propertyName, message);
        if (hasBound) {
            return hasBound;
        }

        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;

            if (mapMessage.itemExists(propertyName)) {
                setValue(pd, target, mapMessage.getObject(propertyName));
                hasBound = true;
            }
        } else {
            throw new NotSupportedMessageRuntimeException(message);
        }
        return hasBound;
    }

    @Override
    protected Object getPayload(Message message) throws JMSException {
        Map<String, Object> map;
        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;

            map = new HashMap<String, Object>();
            for (final String name : new IterableAdapter(message.getPropertyNames())) {
                map.put(name, mapMessage.getObject(name));
            }
        } else {
            throw new NotSupportedMessageRuntimeException(message);
        }
        return map;
    }

    public Class<? extends Message> getTargetMessageClass() {
        return MapMessage.class;
    }
}
