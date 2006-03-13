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

import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.jms.container.annotation.JMSPayload;
import org.seasar.jms.container.annotation.OnMessage;
import org.seasar.jms.core.MessageSender;

/**
 * @author y-komori
 * 
 */
public class JMSContainerTest extends S2TestCase {
    protected TransactionManager tm;
    protected MessageSender sender;
    protected TestAction1 testAction1;
    protected TestAction1 testAction2;

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
        assertEquals(1, testAction1.getCallCount());
        assertEquals("TextPayloadMessage", testAction1.getTextPaylord());
        assertEquals(1, testAction2.getCallCount());
        assertEquals("TextPayloadMessage", testAction2.getTextPaylord());
    }
    
    public static class TestAction1 {
        protected int callCount;
        protected String textPaylord;

        @OnMessage
        public void caller() {
            callCount++;
        }
        
        public int getCallCount() {
            return callCount;
        }
        
        @JMSPayload
        public void setTextPaylord(String textPaylord) {
            this.textPaylord = textPaylord;
        }

        public String getTextPaylord() {
            return textPaylord;
        }
    }

    public abstract static class AbstractTestAction {
        protected int callCount;
        
        @OnMessage
        public void caller() {
            callCount++;
        }
        
        public int getCallCount() {
            return callCount;
        }
    }
    
    public static class TestAction2 extends AbstractTestAction{
        private String textPaylord;
        
        @JMSPayload
        public void setTextPaylord(String textPaylord) {
            this.textPaylord = textPaylord;
        }

        public String getTextPaylord() {
            return textPaylord;
        }
    }
}
