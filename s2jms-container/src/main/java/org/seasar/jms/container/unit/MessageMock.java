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

    public boolean getBooleanProperty(String name) throws JMSException {
        return Boolean.class.cast(properties.get(name));
    }

    public byte getByteProperty(String name) throws JMSException {
        return Byte.class.cast(properties.get(name));
    }

    public double getDoubleProperty(String name) throws JMSException {
        return Double.class.cast(properties.get(name));
    }

    public float getFloatProperty(String name) throws JMSException {
        return Float.class.cast(properties.get(name));
    }

    public int getIntProperty(String name) throws JMSException {
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

    public long getLongProperty(String name) throws JMSException {
        return Long.class.cast(properties.get(name));
    }

    public Object getObjectProperty(String name) throws JMSException {
        return properties.get(name);
    }

    public Enumeration getPropertyNames() throws JMSException {
        return new EnumerationAdapter(properties.keySet().iterator());
    }

    public short getShortProperty(String name) throws JMSException {
        return Short.class.cast(properties.get(name));
    }

    public String getStringProperty(String name) throws JMSException {
        return String.class.cast(properties.get(name));
    }

    public boolean propertyExists(String name) throws JMSException {
        return properties.containsKey(name);
    }

    public void setBooleanProperty(String name, boolean value)
            throws JMSException {
        properties.put(name, value);
    }

    public void setByteProperty(String name, byte value) throws JMSException {
        properties.put(name, value);
    }

    public void setDoubleProperty(String name, double value)
            throws JMSException {
        properties.put(name, value);
    }

    public void setFloatProperty(String name, float value) throws JMSException {
        properties.put(name, value);
    }

    public void setIntProperty(String name, int value) throws JMSException {
        properties.put(name, value);
    }

    public void setJMSCorrelationID(String JMSCorrelationID)
            throws JMSException {
        this.JMSCorrelationID = JMSCorrelationID;
    }

    public void setJMSCorrelationIDAsBytes(byte[] JMSCorrelationIDAsBytes)
            throws JMSException {
        this.JMSCorrelationIDAsBytes = JMSCorrelationIDAsBytes;
    }

    public void setJMSDeliveryMode(int JMSDeliveryMode) throws JMSException {
        this.JMSDeliveryMode = JMSDeliveryMode;
    }

    public void setJMSDestination(Destination JMSDestination)
            throws JMSException {
        this.JMSDestination = JMSDestination;
    }

    public void setJMSExpiration(long JMSExpiration) throws JMSException {
        this.JMSExpiration = JMSExpiration;
    }

    public void setJMSMessageID(String JMSMessageID) throws JMSException {
        this.JMSMessageID = JMSMessageID;
    }

    public void setJMSPriority(int JMSPriority) throws JMSException {
        this.JMSPriority = JMSPriority;
    }

    public void setJMSRedelivered(boolean JMSRedelivered) throws JMSException {
        this.JMSRedelivered = JMSRedelivered;
    }

    public void setJMSReplyTo(Destination JMSReplyTo) throws JMSException {
        this.JMSReplyTo = JMSReplyTo;
    }

    public void setJMSTimestamp(long JMSTimestamp) throws JMSException {
        this.JMSTimestamp = JMSTimestamp;
    }

    public void setJMSType(String JMSType) throws JMSException {
        this.JMSType = JMSType;
    }

    public void setLongProperty(String name, long value) throws JMSException {
        properties.put(name, value);
    }

    public void setObjectProperty(String name, Object value)
            throws JMSException {
        properties.put(name, value);
    }

    public void setShortProperty(String name, short value) throws JMSException {
        properties.put(name, value);
    }

    public void setStringProperty(String name, String value)
            throws JMSException {
        properties.put(name, value);
    }

}
