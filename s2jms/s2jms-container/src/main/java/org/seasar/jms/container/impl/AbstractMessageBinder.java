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

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jms.container.MessageBinder;
import org.seasar.jms.core.exception.SJMSRuntimeException;

public abstract class AbstractMessageBinder implements MessageBinder {
    private static final String JMS_DELIVERY_MODE = "jmsDeliveryMode";
    private static final String JMS_MESSAGE_ID = "jmsMessageID";
    private static final String JMS_TIMESTAMP = "jmsTimestamp";
    private static final String JMS_EXPIRATION = "jmsExpiration";
    private static final String JMS_REDELIVERED = "jmsRedelivered";
    private static final String JMS_PRIORITY = "jmsPriority";
    private static final String JMS_CORRELATION_ID = "jmsCorrelationID";
    private static final String JMS_TYPE = "jmsType";
    private static final String DELIVERY_MODE = "deliveryMode";
    private static final String MESSAGE_ID = "messageID";
    private static final String TIMESTAMP = "timestamp";
    private static final String EXPIRATION = "expiration";
    private static final String REDELIVERED = "redelivered";
    private static final String PRIORITY = "priority";
    private static final String CORRELATION_ID = "correlationID";
    private static final String TYPE = "type";

    public void bindMessage(final Object target, final Message message) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(target.getClass());
        int propertySize = beanDesc.getPropertyDescSize();

        for (int idx = 0; idx < propertySize; idx++) {
            PropertyDesc pd = beanDesc.getPropertyDesc(idx);
            if (pd.hasWriteMethod()) {
                String propertyName = pd.getPropertyName();

                try {
                    // Binds payload
                    boolean hasBound = bindPayload(pd, target, propertyName, message);
                    if (hasBound) {
                        continue;
                    }

                    // Binds property
                    if (message.propertyExists(propertyName)) {
                        setValue(pd, target, message.getObjectProperty(propertyName));
                    } else {
                        // Binds header
                        Object headerValue = getJMSHeader(message, propertyName);
                        if (headerValue != null) {
                            setValue(pd, target, headerValue);
                        }
                    }
                } catch (JMSException ex) {
                    throw new SJMSRuntimeException("EJMS0001", ex);
                }
            }
        }
    }

    private Object getJMSHeader(final Message message, final String headerName) {
        Object headerValue = null;
        try {
            if (JMS_DELIVERY_MODE.equals(headerName) || DELIVERY_MODE.equals(headerName)) {
                headerValue = new Integer(message.getJMSDeliveryMode());
            } else if (JMS_MESSAGE_ID.equals(headerName) || MESSAGE_ID.equals(headerName)) {
                headerValue = message.getJMSMessageID();
            } else if (JMS_TIMESTAMP.equals(headerName) || TIMESTAMP.equals(headerName)) {
                headerValue = new Long(message.getJMSTimestamp());
            } else if (JMS_EXPIRATION.equals(headerName) || EXPIRATION.equals(headerName)) {
                headerValue = new Long(message.getJMSExpiration());
            } else if (JMS_REDELIVERED.equals(headerName) || REDELIVERED.equals(headerName)) {
                headerValue = new Boolean(message.getJMSRedelivered());
            } else if (JMS_PRIORITY.equals(headerName) || PRIORITY.equals(headerName)) {
                headerValue = new Integer(message.getJMSPriority());
            } else if (JMS_CORRELATION_ID.equals(headerName) || CORRELATION_ID.equals(headerName)) {
                headerValue = message.getJMSCorrelationID();
            } else if (JMS_TYPE.equals(headerName) || TYPE.equals(headerName)) {
                headerValue = message.getJMSType();
            }
        } catch (JMSException ex) {
            throw new SJMSRuntimeException("EJMS0001", ex);
        }

        return headerValue;
    }

    protected void setValue(final PropertyDesc pd, final Object target, final Object value) {
        pd.setValue(target, pd.convertIfNeed(value));
    }

    abstract protected boolean bindPayload(final PropertyDesc pd, final Object target,
            final String propertyName, Message message) throws JMSException;
}
