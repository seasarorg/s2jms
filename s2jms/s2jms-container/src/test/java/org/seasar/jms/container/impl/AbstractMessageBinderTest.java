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

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.jca.unit.S2EasyMockTestCase;

import static org.easymock.EasyMock.expect;

/**
 * @author y-komori
 * 
 */
public class AbstractMessageBinderTest extends S2EasyMockTestCase {
    protected BindTarget bindTarget;

    private AbstractMessageBinder messageBinder;

    private TextMessage message;

    private final Object testObject = new Object();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(this.getClass().getSimpleName() + ".dicon");

        messageBinder = new TestMessageBinder();
        message = createNiceMock(TextMessage.class);
    }

    public void testBindMessage() throws Exception {
        new Subsequence() {
            @Override
            protected void replay() throws Exception {
                messageBinder.bindMessage(bindTarget, message);

                assertEquals("JMSDeliveryMode", 123, bindTarget.getJmsDeliveryMode());
                assertEquals("JMSMessageID", "TestMessageID", bindTarget.getJmsMessageID());
                assertEquals("JMSTimestamp", 123456789L, bindTarget.getJmsTimestamp());
                assertEquals("JMSExpiration", 987654321L, bindTarget.getJmsExpiration());
                assertEquals("JMSRedelivered", true, bindTarget.isJmsRedelivered());
                assertEquals("JMSPriority", 3, bindTarget.getJmsPriority());
                assertEquals("JMSCorrelationID", "TestCorrelationID", bindTarget
                        .getJmsCorrelationID());
                assertEquals("JMSType", "TestJMSType", bindTarget.getJmsType());

                assertEquals("DeliveryMode", 123, bindTarget.getDeliveryMode());
                assertEquals("MessageID", "TestMessageID", bindTarget.getMessageID());
                assertEquals("Timestamp", 123456789L, bindTarget.getTimestamp());
                assertEquals("Expiration", 987654321L, bindTarget.getExpiration());
                assertEquals("Redelivered", true, bindTarget.isRedelivered());
                assertEquals("Priority", 3, bindTarget.getPriority());
                assertEquals("CorrelationID", "TestCorrelationID", bindTarget.getCorrelationID());
                assertEquals("Type", "TestJMSType", bindTarget.getType());
                
                assertEquals("booleanProperty", true, bindTarget.isBooleanProperty());
                assertEquals("byteProperty", Byte.MAX_VALUE, bindTarget.getByteProperty());
                assertEquals("doubleProperty", Double.MAX_VALUE, bindTarget.getDoubleProperty());
                assertEquals("floatProperty", Float.MAX_VALUE, bindTarget.getFloatProperty());
                assertEquals("intProperty", Integer.MAX_VALUE, bindTarget.getIntProperty());
                assertEquals("longProperty", Long.MAX_VALUE, bindTarget.getLongProperty());
                assertEquals("objectProperty", testObject, bindTarget.getObjectProperty());
                assertEquals("shortProperty", Short.MAX_VALUE, bindTarget.getShortProperty());
                assertEquals("stringProperty", "StringProperty", bindTarget.getStringProperty());
            }

            @Override
            protected void verify() throws Exception {
                expect(message.getJMSDeliveryMode()).andReturn(123).anyTimes();
                expect(message.getJMSMessageID()).andReturn("TestMessageID").anyTimes();
                expect(message.getJMSTimestamp()).andReturn(123456789L).anyTimes();
                expect(message.getJMSExpiration()).andReturn(987654321L).anyTimes();
                expect(message.getJMSRedelivered()).andReturn(true).anyTimes();
                expect(message.getJMSPriority()).andReturn(3).anyTimes();
                expect(message.getJMSCorrelationID()).andReturn("TestCorrelationID").anyTimes();
                expect(message.getJMSType()).andReturn("TestJMSType").anyTimes();

                expect(message.propertyExists("booleanProperty")).andReturn(true).anyTimes();
                expect(message.propertyExists("byteProperty")).andReturn(true).anyTimes();
                expect(message.propertyExists("doubleProperty")).andReturn(true).anyTimes();
                expect(message.propertyExists("floatProperty")).andReturn(true).anyTimes();
                expect(message.propertyExists("intProperty")).andReturn(true).anyTimes();
                expect(message.propertyExists("longProperty")).andReturn(true).anyTimes();
                expect(message.propertyExists("objectProperty")).andReturn(true).anyTimes();
                expect(message.propertyExists("shortProperty")).andReturn(true).anyTimes();
                expect(message.propertyExists("stringProperty")).andReturn(true).anyTimes();

                expect(message.getObjectProperty("booleanProperty")).andReturn(new Boolean(true))
                        .anyTimes();
                expect(message.getObjectProperty("byteProperty")).andReturn(
                        new Byte(Byte.MAX_VALUE)).anyTimes();
                expect(message.getObjectProperty("doubleProperty")).andReturn(
                        new Double(Double.MAX_VALUE)).anyTimes();
                expect(message.getObjectProperty("floatProperty")).andReturn(
                        new Float(Float.MAX_VALUE)).anyTimes();
                expect(message.getObjectProperty("intProperty")).andReturn(
                        new Integer(Integer.MAX_VALUE)).anyTimes();
                expect(message.getObjectProperty("longProperty")).andReturn(
                        new Long(Long.MAX_VALUE)).anyTimes();
                expect(message.getObjectProperty("objectProperty")).andReturn(testObject)
                        .anyTimes();
                expect(message.getObjectProperty("shortProperty")).andReturn(
                        new Short(Short.MAX_VALUE)).anyTimes();
                expect(message.getObjectProperty("stringProperty")).andReturn("StringProperty")
                        .anyTimes();

            }
        }.doTest();
    }

    public class TestMessageBinder extends AbstractMessageBinder {
        @Override
        protected boolean bindPayload(PropertyDesc pd, Object target, String propertyName,
                Message message) throws JMSException {
            return false;
        }

        public Class<? extends Message> getMessageType() {
            return TextMessage.class;
        }
    }

    public static class BindTarget {
        private int deliveryMode;
        private String messageID;
        private long timestamp;
        private long expiration;
        private boolean redelivered;
        private int priority;
        private String correlationID;
        private String type;

        private int jmsDeliveryMode;
        private String jmsMessageID;
        private long jmsTimestamp;
        private long jmsExpiration;
        private boolean jmsRedelivered;
        private int jmsPriority;
        private String jmsCorrelationID;
        private String jmsType;

        private boolean booleanProperty;
        private byte byteProperty;
        private double doubleProperty;
        private float floatProperty;
        private int intProperty;
        private long longProperty;
        private Object objectProperty;
        private short shortProperty;
        private String stringProperty;

        public String getCorrelationID() {
            return correlationID;
        }

        public void setCorrelationID(String correlationID) {
            this.correlationID = correlationID;
        }

        public int getDeliveryMode() {
            return deliveryMode;
        }

        public void setDeliveryMode(int deliveryMode) {
            this.deliveryMode = deliveryMode;
        }

        public long getExpiration() {
            return expiration;
        }

        public void setExpiration(long expiration) {
            this.expiration = expiration;
        }

        public String getMessageID() {
            return messageID;
        }

        public void setMessageID(String messageID) {
            this.messageID = messageID;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public boolean isRedelivered() {
            return redelivered;
        }

        public void setRedelivered(boolean redelivered) {
            this.redelivered = redelivered;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getJmsCorrelationID() {
            return jmsCorrelationID;
        }

        public void setJmsCorrelationID(String jmsCorrelationID) {
            this.jmsCorrelationID = jmsCorrelationID;
        }

        public int getJmsDeliveryMode() {
            return jmsDeliveryMode;
        }

        public void setJmsDeliveryMode(int jmsDeliveryMode) {
            this.jmsDeliveryMode = jmsDeliveryMode;
        }

        public long getJmsExpiration() {
            return jmsExpiration;
        }

        public void setJmsExpiration(long jmsExpiration) {
            this.jmsExpiration = jmsExpiration;
        }

        public String getJmsMessageID() {
            return jmsMessageID;
        }

        public void setJmsMessageID(String jmsMessageID) {
            this.jmsMessageID = jmsMessageID;
        }

        public int getJmsPriority() {
            return jmsPriority;
        }

        public void setJmsPriority(int jmsPriority) {
            this.jmsPriority = jmsPriority;
        }

        public boolean isJmsRedelivered() {
            return jmsRedelivered;
        }

        public void setJmsRedelivered(boolean jmsRedelivered) {
            this.jmsRedelivered = jmsRedelivered;
        }

        public long getJmsTimestamp() {
            return jmsTimestamp;
        }

        public void setJmsTimestamp(long jmsTimestamp) {
            this.jmsTimestamp = jmsTimestamp;
        }

        public String getJmsType() {
            return jmsType;
        }

        public void setJmsType(String jmsType) {
            this.jmsType = jmsType;
        }

        public boolean isBooleanProperty() {
            return booleanProperty;
        }

        public void setBooleanProperty(boolean booleanProperty) {
            this.booleanProperty = booleanProperty;
        }

        public byte getByteProperty() {
            return byteProperty;
        }

        public void setByteProperty(byte bytePropertry) {
            this.byteProperty = bytePropertry;
        }

        public double getDoubleProperty() {
            return doubleProperty;
        }

        public void setDoubleProperty(double doubleProperty) {
            this.doubleProperty = doubleProperty;
        }

        public float getFloatProperty() {
            return floatProperty;
        }

        public void setFloatProperty(float floatProperty) {
            this.floatProperty = floatProperty;
        }

        public int getIntProperty() {
            return intProperty;
        }

        public void setIntProperty(int intProperty) {
            this.intProperty = intProperty;
        }

        public long getLongProperty() {
            return longProperty;
        }

        public void setLongProperty(long longProperty) {
            this.longProperty = longProperty;
        }

        public Object getObjectProperty() {
            return objectProperty;
        }

        public void setObjectProperty(Object objectProperty) {
            this.objectProperty = objectProperty;
        }

        public short getShortProperty() {
            return shortProperty;
        }

        public void setShortProperty(short shortProperty) {
            this.shortProperty = shortProperty;
        }

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }
    }
}
