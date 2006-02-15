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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.message.MessageFactory;

/**
 * @author koichik
 */
public abstract class AbstractMessageFactory<MSGTYPE extends Message> implements
        MessageFactory<MSGTYPE> {
    protected String correlationId;
    protected byte[] correlationIdAsBytes;
    protected Map<String, Object> properties = new HashMap<String, Object>();

    public AbstractMessageFactory() {
    }

    public String getCorrelationId() {
        return correlationId;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setCorrelationId(final String correlationId) {
        this.correlationId = correlationId;
        this.correlationIdAsBytes = null;
    }

    public byte[] getCorrelationIdAsBytes() {
        return correlationIdAsBytes;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setCorrelationIdAsBytes(final byte[] correlationIdAsBytes) {
        this.correlationIdAsBytes = correlationIdAsBytes;
        this.correlationId = null;
    }

    public Object getProperty(final String name) {
        return properties.get(name);
    }

    public void addProperty(final String name, final Object value) {
        properties.put(name, value);
    }

    public MSGTYPE createMessage(final Session session) {
        try {
            final MSGTYPE message = createMessageInstance(session);
            setupHeader(message);
            setupProperties(message);
            setupPayload(message);
            return message;
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    protected abstract MSGTYPE createMessageInstance(final Session session) throws JMSException;

    protected void setupHeader(final Message message) throws JMSException {
        if (correlationId != null) {
            message.setJMSCorrelationID(correlationId);
        } else if (correlationIdAsBytes != null) {
            message.setJMSCorrelationIDAsBytes(correlationIdAsBytes);
        }
    }

    protected void setupProperties(final Message message) throws JMSException {
        for (final Map.Entry<String, Object> entry : properties.entrySet()) {
            final String name = entry.getKey();
            final Object value = entry.getValue();
            if (value instanceof Boolean) {
                message.setBooleanProperty(name, (Boolean) value);
            } else if (value instanceof Byte) {
                message.setByteProperty(name, (Byte) value);
            } else if (value instanceof Short) {
                message.setShortProperty(name, (Short) value);
            } else if (value instanceof Integer) {
                message.setIntProperty(name, (Integer) value);
            } else if (value instanceof Long) {
                message.setLongProperty(name, (Long) value);
            } else if (value instanceof Float) {
                message.setFloatProperty(name, (Float) value);
            } else if (value instanceof Double) {
                message.setDoubleProperty(name, (Double) value);
            } else if (value instanceof String) {
                message.setStringProperty(name, (String) value);
            } else {
                message.setObjectProperty(name, value);
            }
        }
    }

    protected abstract void setupPayload(MSGTYPE message) throws JMSException;
}
