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
package org.seasar.jms.container;

import javax.jms.Message;
import javax.transaction.TransactionManager;

import org.seasar.jms.container.annotation.OnMessage;
import org.seasar.jms.container.unit.S2JMSTestCase;
import org.seasar.jms.core.MessageSender;

/**
 * @author y-komori
 * 
 */
public class JMSContainerTest extends S2JMSTestCase {

    protected TransactionManager tm;
    protected MessageSender sender;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("jms-wmq-inbound.dicon");
        include("jms-wmq-outbound.dicon");
    }

    public void test() throws Exception {
        // tm.begin();
        // try {
        // sender.send("TextPayloadMessage");
        // }
        // finally {
        // tm.commit();
        // }
        // Thread.sleep(500);
        assertEquals(1, MessageListener1.getCallCount());
        assertEquals("TextPayloadMessage", MessageListener1.getPayload());
        assertEquals(1, MessageListener2.getCallCount());
        assertEquals("TextPayloadMessage", MessageListener2.textPayload);
        assertNotNull(MessageListener2.message);
    }

    public static class MessageListener1 {

        protected static int callCount;
        protected static String payload;
        protected MessageSender sender;

        @OnMessage
        public void caller() {
            callCount++;
            sender.send("Received.");
        }

        public static int getCallCount() {
            return callCount;
        }

        public static void setPayload(String payload) {
            MessageListener1.payload = payload;
        }

        public static String getPayload() {
            return payload;
        }

        public void setSender(MessageSender sender) {
            this.sender = sender;
        }
    }

    public abstract static class AbstractMessageListener {

        protected static int callCount;

        @OnMessage
        public void caller() {
            callCount++;
        }

        public static int getCallCount() {
            return callCount;
        }
    }

    public static class MessageListener2 extends AbstractMessageListener {

        private static String textPayload;

        protected static Message message;

        public void setMessage(Message message) {
            MessageListener2.message = message;
        }

        public Message getMessage() {
            return message;
        }

        public String getPayload() {
            return textPayload;
        }

        public void setPayload(String textPayload) {
            MessageListener2.textPayload = textPayload;
        }
    }
}
