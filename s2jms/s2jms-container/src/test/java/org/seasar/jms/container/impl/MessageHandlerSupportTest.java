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

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;

import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jca.unit.EasyMockTestCase;
import org.seasar.jms.container.annotation.JMSHeader;
import org.seasar.jms.container.annotation.JMSPayload;
import org.seasar.jms.container.annotation.JMSProperty;
import org.seasar.jms.container.annotation.MessageHandler;
import org.seasar.jms.container.exception.IllegalMessageHandlerException;
import org.seasar.jms.container.exception.MessageHandlerNotFoundException;
import org.seasar.jms.container.exception.NotBoundException;

import static org.easymock.EasyMock.expect;

public class MessageHandlerSupportTest extends EasyMockTestCase {
    Message message;

    public MessageHandlerSupportTest() {
    }

    public MessageHandlerSupportTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        message = createMock(Message.class);
    }

    public void test() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("hoge hoge", "HOGE HOGE");
                map.put("dummy", "DUMMY");

                MessageHandlerSupport support = new MessageHandlerSupport(Test1.class);
                assertEquals("1", 8, support.binders.size());
                assertEquals("2", "onMessage", support.method.getName());

                Test1 test = new Test1();
                support.bind(test, message, map);
                assertEquals("3", test.correlationID, "abcd");
                assertEquals("4", test.mode, 100);
                assertEquals("5", test.JMSMessageID, "hogehoge");
                assertEquals("6", test.priority, -10);
                assertEquals("7", test.foo, 1000);
                assertEquals("8", test.bar, "BAZ");
                assertSame("9", test.body, map);
                assertEquals("10", test.hoge, "HOGE HOGE");
                assertNull("11", test.dummy);

                support.invoke(test);
                assertTrue("12", test.called);
            }

            @Override
            public void verify() throws Exception {
                expect(message.getJMSCorrelationID()).andReturn("abcd");
                expect(message.getJMSDeliveryMode()).andReturn(100);
                expect(message.getJMSMessageID()).andReturn("hogehoge");
                expect(message.getJMSPriority()).andReturn(-10);
                expect(message.getObjectProperty("foo")).andReturn(1000);
                expect(message.getObjectProperty("baz")).andReturn("BAZ");
            }
        }.doTest();

    }

    public void testUnbound() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                MessageHandlerSupport support = new MessageHandlerSupport(Test2.class);
                assertEquals("1", 1, support.binders.size());
                assertEquals("2", "onMessage", support.method.getName());

                Test2 test = new Test2();
                try {
                    support.bind(test, message, null);
                    fail("3");
                } catch (NotBoundException expected) {
                    System.out.println(expected);
                }
            }

            @Override
            public void verify() throws Exception {
                expect(message.getJMSMessageID()).andReturn(null);
            }
        }.doTest();

    }

    public void testMethod() throws Exception {
        MessageHandlerSupport support = new MessageHandlerSupport(Test3.class);
        assertEquals("1", 0, support.binders.size());
        assertEquals("2", "invoke", support.method.getName());

        Test3 test = new Test3();
        support.invoke(test);
        assertTrue("3", test.called);
    }

    public void testMethodNotFound() throws Exception {
        try {
            new MessageHandlerSupport(Test4.class);
            fail("1");
        } catch (MessageHandlerNotFoundException expected) {
            System.out.println(expected);
        }
    }

    public void testIllegalMethod() throws Exception {
        try {
            new MessageHandlerSupport(Test5.class);
            fail("1");
        } catch (IllegalMessageHandlerException expected) {
            System.out.println(expected);
        }
    }

    public static class Test1 {
        @JMSHeader
        String correlationID;

        @JMSHeader(name = "deliveryMode")
        int mode;

        @JMSHeader(bindingType = BindingType.MUST)
        String JMSMessageID;

        int priority;

        @JMSHeader(name = "JMSPriority", bindingType = BindingType.MUST)
        public void setPriority(int priority) {
            this.priority = priority;
        }

        @JMSProperty
        int foo;

        String bar;

        @JMSProperty(name = "baz")
        public void setBar(String bar) {
            this.bar = bar;
        }

        @JMSPayload
        Map body;

        String hoge;

        @JMSPayload(name = "hoge hoge")
        public void setHoge(String hoge) {
            this.hoge = hoge;
        }

        String dummy;

        boolean called;

        public void onMessage() {
            called = true;
        }
    }

    public static class Test2 {
        @JMSHeader(bindingType = BindingType.MUST)
        String JMSMessageID;

        public void onMessage() {
        }
    }

    public static class Test3 {
        boolean called;

        @MessageHandler
        public void invoke() {
            called = true;
        }
    }

    public static class Test4 {
        boolean called;

        public void invoke() {
            called = true;
        }
    }

    public static class Test5 {
        boolean called;

        @MessageHandler
        public void invoke(boolean b) {
            called = b;
        }
    }
}
