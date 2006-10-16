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
package org.seasar.jms.container.unit;

import java.util.Enumeration;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class MessageMock implements Message {
    protected String JMSCorrelationID;

    protected byte[] JMSCorrelationIDAsBytes;

    protected int JMSDeliveryMode = DEFAULT_DELIVERY_MODE;

    protected Destination JMSDestination;

    protected long JMSExpiration = DEFAULT_TIME_TO_LIVE;

    protected String JMSMessageID;

    protected int JMSPriority = DEFAULT_PRIORITY;

    protected boolean JMSRedelivered;

    protected Destination JMSReplyTo;

    protected long JMSTimestamp;

    protected String JMSType;

    protected Map<String, Object> properties = CollectionsUtil.newHashMap();

    public MessageMock() {
    }

    public void acknowledge() throws JMSException {
    }

    public void clearBody() throws JMSException {
    }

    public void clearProperties() throws JMSException {
        properties.clear();
    }

    public boolean getBooleanProperty(final String name) throws JMSException {
        return Boolean.class.cast(properties.get(name));
    }

    public byte getByteProperty(final String name) throws JMSException {
        return Byte.class.cast(properties.get(name));
    }

    public double getDoubleProperty(final String name) throws JMSException {
        return Double.class.cast(properties.get(name));
    }

    public float getFloatProperty(final String name) throws JMSException {
        return Float.class.cast(properties.get(name));
    }

    public int getIntProperty(final String name) throws JMSException {
        return Integer.class.cast(properties.get(name));
    }

    public String getJMSCorrelationID() throws JMSException {
        return JMSCorrelationID;
    }

    public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
        return JMSCorrelationIDAsBytes;
    }

    public int getJMSDeliveryMode() throws JMSException {
        return JMSDeliveryMode;
    }

    public Destination getJMSDestination() throws JMSException {
        return JMSDestination;
    }

    public long getJMSExpiration() throws JMSException {
        return JMSExpiration;
    }

    public String getJMSMessageID() throws JMSException {
        return JMSMessageID;
    }

    public int getJMSPriority() throws JMSException {
        return JMSPriority;
    }

    public boolean getJMSRedelivered() throws JMSException {
        return JMSRedelivered;
    }

    public Destination getJMSReplyTo() throws JMSException {
        return JMSReplyTo;
    }

    public long getJMSTimestamp() throws JMSException {
        return JMSTimestamp;
    }

    public String getJMSType() throws JMSException {
        return JMSType;
    }

    public long getLongProperty(final String name) throws JMSException {
        return Long.class.cast(properties.get(name));
    }

    public Object getObjectProperty(final String name) throws JMSException {
        return properties.get(name);
    }

    public Enumeration getPropertyNames() throws JMSException {
        return new EnumerationAdapter(properties.keySet().iterator());
    }

    public short getShortProperty(final String name) throws JMSException {
        return Short.class.cast(properties.get(name));
    }

    public String getStringProperty(final String name) throws JMSException {
        return String.class.cast(properties.get(name));
    }

    public boolean propertyExists(final String name) throws JMSException {
        return properties.containsKey(name);
    }

    public void setBooleanProperty(final String name, final boolean value) throws JMSException {
        properties.put(name, value);
    }

    public void setByteProperty(final String name, final byte value) throws JMSException {
        properties.put(name, value);
    }

    public void setDoubleProperty(final String name, final double value) throws JMSException {
        properties.put(name, value);
    }

    public void setFloatProperty(final String name, final float value) throws JMSException {
        properties.put(name, value);
    }

    public void setIntProperty(final String name, final int value) throws JMSException {
        properties.put(name, value);
    }

    public void setJMSCorrelationID(final String JMSCorrelationID) throws JMSException {
        this.JMSCorrelationID = JMSCorrelationID;
    }

    public void setJMSCorrelationIDAsBytes(final byte[] JMSCorrelationIDAsBytes)
            throws JMSException {
        this.JMSCorrelationIDAsBytes = JMSCorrelationIDAsBytes;
    }

    public void setJMSDeliveryMode(final int JMSDeliveryMode) throws JMSException {
        this.JMSDeliveryMode = JMSDeliveryMode;
    }

    public void setJMSDestination(final Destination JMSDestination) throws JMSException {
        this.JMSDestination = JMSDestination;
    }

    public void setJMSExpiration(final long JMSExpiration) throws JMSException {
        this.JMSExpiration = JMSExpiration;
    }

    public void setJMSMessageID(final String JMSMessageID) throws JMSException {
        this.JMSMessageID = JMSMessageID;
    }

    public void setJMSPriority(final int JMSPriority) throws JMSException {
        this.JMSPriority = JMSPriority;
    }

    public void setJMSRedelivered(final boolean JMSRedelivered) throws JMSException {
        this.JMSRedelivered = JMSRedelivered;
    }

    public void setJMSReplyTo(final Destination JMSReplyTo) throws JMSException {
        this.JMSReplyTo = JMSReplyTo;
    }

    public void setJMSTimestamp(final long JMSTimestamp) throws JMSException {
        this.JMSTimestamp = JMSTimestamp;
    }

    public void setJMSType(final String JMSType) throws JMSException {
        this.JMSType = JMSType;
    }

    public void setLongProperty(final String name, final long value) throws JMSException {
        properties.put(name, value);
    }

    public void setObjectProperty(final String name, final Object value) throws JMSException {
        properties.put(name, value);
    }

    public void setShortProperty(final String name, final short value) throws JMSException {
        properties.put(name, value);
    }

    public void setStringProperty(final String name, final String value) throws JMSException {
        properties.put(name, value);
    }

}
