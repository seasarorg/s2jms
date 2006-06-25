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
import org.seasar.jms.container.exception.IllegalMessageHandlerException;
import org.seasar.jms.container.exception.MessageHandlerNotFoundException;
import org.seasar.jms.container.unit.MessageMock;

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
        assertEquals("onMessage", support.method.getName());

        Test1 test = new Test1();
        support.invoke(test);
        assertTrue(test.called);
    }

    public void testMethod() throws Exception {
        MessageListenerSupport support = new MessageListenerSupport(Test2.class);
        assertEquals("2", "invoke", support.method.getName());

        Test2 test = new Test2();
        support.invoke(test);
        assertTrue("3", test.called);
    }

    public void testMethodNotFound() throws Exception {
        try {
            new MessageListenerSupport(Test3.class);
            fail("1");
        } catch (MessageHandlerNotFoundException expected) {
            System.out.println(expected);
        }
    }

    public void testIllegalMethod() throws Exception {
        try {
            new MessageListenerSupport(Test4.class);
            fail("1");
        } catch (IllegalMessageHandlerException expected) {
            System.out.println(expected);
        }
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

        @OnMessage
        public void invoke(boolean b) {
            called = b;
        }
    }
}
