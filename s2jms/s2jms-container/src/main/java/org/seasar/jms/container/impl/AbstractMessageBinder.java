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

public abstract class AbstractMessageBinder implements MessageBinder {
    private static final String JMS_PREFIX = "JMS";
    private static final String JMS_DELIVERY_MODE = "JMSDeliveryMode";
    private static final String JMS_MESSAGE_ID = "JMSMessageID";
    private static final String JMS_TIMESTAMP = "JMSTimestamp";
    private static final String JMS_EXPIRATION = "JMSExpiration";
    private static final String JMS_REDELIVERED = "JMSRedelivered";
    private static final String JMS_PRIORITY = "JMSPriority";
    private static final String JMS_CORRELATION_ID = "JMSCorrelationID";
    private static final String JMS_TYPE = "JMSType";

    public void bindMessage(Object target, Message message) {
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
                } catch (JMSException e) {
                    // TODO: handle exception
                }
            }
        }
    }

    private Object getJMSHeader(Message message, String headerName) {
        Object headerValue = null;
        try {
            if (JMS_DELIVERY_MODE.equals(headerName)
                    || (JMS_PREFIX + JMS_DELIVERY_MODE).equals(headerName)) {
                headerValue = new Integer(message.getJMSDeliveryMode());
            } else if (JMS_MESSAGE_ID.equals(headerName)
                    || (JMS_PREFIX + JMS_MESSAGE_ID).equals(headerName)) {
                headerValue = message.getJMSMessageID();
            } else if (JMS_TIMESTAMP.equals(headerName)
                    || (JMS_PREFIX + JMS_TIMESTAMP).equals(headerName)) {
                headerValue = new Long(message.getJMSTimestamp());
            } else if (JMS_EXPIRATION.equals(headerName)
                    || (JMS_PREFIX + JMS_EXPIRATION).equals(headerName)) {
                headerValue = new Long(message.getJMSExpiration());
            } else if (JMS_REDELIVERED.equals(headerName)
                    || (JMS_PREFIX + JMS_REDELIVERED).equals(headerName)) {
                headerValue = new Boolean(message.getJMSRedelivered());
            } else if (JMS_PRIORITY.equals(headerName)
                    || (JMS_PREFIX + JMS_PRIORITY).equals(headerName)) {
                headerValue = new Integer(message.getJMSPriority());
            } else if (JMS_CORRELATION_ID.equals(headerName)
                    || (JMS_PREFIX + JMS_CORRELATION_ID).equals(headerName)) {
                headerValue = message.getJMSCorrelationID();
            } else if (JMS_TYPE.equals(headerName) || (JMS_PREFIX + JMS_TYPE).equals(headerName)) {
                headerValue = message.getJMSType();
            }
        } catch (JMSException ex) {
            // TODO ��O����
        }

        return headerValue;
    }

    protected void setValue(PropertyDesc pd, Object target, Object value) {
        pd.setValue(target, pd.convertIfNeed(value));
    }

    abstract protected boolean bindPayload(PropertyDesc pd, Object target, String propertyName,
            Message message) throws JMSException;
}
