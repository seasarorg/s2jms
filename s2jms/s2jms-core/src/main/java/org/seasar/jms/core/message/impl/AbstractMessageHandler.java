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

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.util.IterableAdapter;

/**
 * @author koichik
 */
public abstract class AbstractMessageHandler<MSGTYPE extends Message, T> implements
        MessageHandler<MSGTYPE, T> {
    protected MSGTYPE message;

    public AbstractMessageHandler() {
    }

    public T handleMessage(final MSGTYPE message) {
        try {
            this.message = message;
            return getPayload();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public abstract T getPayload() throws JMSException;

    public MSGTYPE getMessage() {
        return message;
    }

    public void acknowledge() {
        try {
            message.acknowledge();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public String getCorrelationID() {
        try {
            return message.getJMSCorrelationID();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public byte[] getCorrelationIDAsBytes() {
        try {
            return message.getJMSCorrelationIDAsBytes();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public int getDeliveryMode() {
        try {
            return message.getJMSDeliveryMode();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public Destination getDestination() {
        try {
            return message.getJMSDestination();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public long getExpiration() {
        try {
            return message.getJMSExpiration();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public String getMessageID() {
        try {
            return message.getJMSMessageID();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public int getPriority() {
        try {
            return message.getJMSPriority();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public boolean getRedelivered() {
        try {
            return message.getJMSRedelivered();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public Destination getReplyTo() {
        try {
            return message.getJMSReplyTo();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public long getTimestamp() {
        try {
            return message.getJMSTimestamp();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public String getType() {
        try {
            return message.getJMSType();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    public Map<String, Object> getProperties() {
        try {
            final Map<String, Object> map = new HashMap<String, Object>();
            for (final String name : new IterableAdapter(message.getPropertyNames())) {
                map.put(name, message.getObjectProperty(name));
            }
            return map;
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }
}
