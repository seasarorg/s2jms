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

import javax.jms.Message;

import junit.framework.TestCase;

import org.seasar.jms.container.annotation.OnMessage;
import org.seasar.jms.container.exception.MessageListenerNotFoundException;
import org.seasar.jms.core.mock.MessageMock;
import org.seasar.jms.core.mock.TextMessageMock;

public class MessageListenerSupportTest extends TestCase {
    Message message;

    public MessageListenerSupportTest() {
    }

    public MessageListenerSupportTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        message = new MessageMock();
    }

    public void test() throws Exception {
        MessageListenerSupport support = new MessageListenerSupport(Test1.class);
        assertEquals("onMessage", support.getListenerMethodName());

        Test1 test = new Test1();
        support.invoke(test, null);
        assertTrue(test.called);
    }

    public void testMethod() throws Exception {
        MessageListenerSupport support = new MessageListenerSupport(Test2.class);
        assertEquals("invoke", support.getListenerMethodName());

        Test2 test = new Test2();
        support.invoke(test, null);
        assertTrue(test.called);
    }

    public void testMethodNotFound() throws Exception {
        try {
            new MessageListenerSupport(Test3.class);
            fail("1");
        } catch (MessageListenerNotFoundException expected) {
            System.out.println(expected);
        }
    }

    public void testMessageParameter() throws Exception {
        MessageListenerSupport support = new MessageListenerSupport(Test4.class);
        assertEquals("invoke", support.getListenerMethodName());

        Test4 test = new Test4();
        support.invoke(test, new TextMessageMock());
        assertTrue(test.called);
    }

    public void testPayloadParameter() throws Exception {
        MessageListenerSupport support = new MessageListenerSupport(Test5.class);
        assertEquals("onMessage", support.getListenerMethodName());

        Test5 test = new Test5();
        support.invoke(test, new TextMessageMock("Hoge"));
        assertTrue(test.called);
        assertEquals("Hoge", test.text);
    }

    public static class Test1 {
        boolean called;

        public void onMessage() {
            called = true;
        }
    }

    public static class Test2 {
        boolean called;

        @OnMessage
        public void invoke() {
            called = true;
        }
    }

    public static class Test3 {
        boolean called;

        public void invoke() {
            called = true;
        }
    }

    public static class Test4 {
        boolean called;
        Message msg;

        @OnMessage
        public void invoke(Message msg) {
            called = true;
            this.msg = msg;
        }
    }

    public static class Test5 {
        boolean called;
        String text;

        @OnMessage
        public void onMessage(String text) {
            called = true;
            this.text = text;
        }
    }

}
