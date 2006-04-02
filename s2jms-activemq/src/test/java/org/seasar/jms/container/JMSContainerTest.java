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

import org.seasar.jms.container.annotation.JMSPayload;
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
    protected MessageListener1 listener1;
    protected MessageListener2 listener2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(this.getClass().getSimpleName() + ".dicon");
        include("cf-test.dicon");
    }

    public void test() throws Exception {
        tm.begin();
        try {
            sender.send("TextPayloadMessage");
        } finally {
            tm.commit();
        }

        Thread.sleep(500);
        assertEquals(1, MessageListener1.getCallCount());
        assertEquals("TextPayloadMessage", MessageListener1.getTextPaylord());
        assertEquals(1, MessageListener2.getCallCount());
        assertEquals("TextPayloadMessage", MessageListener2.getTextPaylord());
        assertNotNull("TextMessage", MessageListener2.getMessage());
    }

    public static class MessageListener1 {
        protected static int callCount;
        protected static String textPaylord;

        @OnMessage
        public void caller() {
            callCount++;
        }

        public static int getCallCount() {
            return callCount;
        }

        @JMSPayload
        public static void setTextPaylord(String paylord) {
            MessageListener1.textPaylord = paylord;
        }

        public static String getTextPaylord() {
            return textPaylord;
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
        protected static String textPaylord;
        protected static Message message;

        public static void setMessage(Message message) {
            MessageListener2.message = message;
        }

        public static Message getMessage() {
            return message;
        }

        public static String getTextPaylord() {
            return textPaylord;
        }

        @JMSPayload
        public static void setTextPaylord(String textPaylord) {
            MessageListener2.textPaylord = textPaylord;
        }
    }
}
