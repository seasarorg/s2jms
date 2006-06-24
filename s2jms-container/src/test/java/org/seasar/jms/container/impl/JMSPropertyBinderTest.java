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

import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.expect;

public class JMSPropertyBinderTest extends EasyMockTestCase {
    Message message;
    String foo;
    int bar;

    public JMSPropertyBinderTest() {
    }

    public JMSPropertyBinderTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        message = createStrictMock(Message.class);
        foo = null;
        bar = 0;
    }

    public void testFoo() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                JMSPropertyBinder binder = new JMSPropertyBinder("foo", BindingType.SHOULD,
                        JMSPropertyBinderTest.class.getDeclaredField("foo"));
                binder.bind(JMSPropertyBinderTest.this, message, null);
                assertEquals("1", foo, "FOO");
            }

            @Override
            public void record() throws Exception {
                expect(message.getObjectProperty("foo")).andReturn("FOO");
            }
        }.doTest();
    }

    public void testBar() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                JMSPropertyBinder binder = new JMSPropertyBinder("bar", BindingType.SHOULD,
                        JMSPropertyBinderTest.class.getDeclaredField("bar"));
                binder.bind(JMSPropertyBinderTest.this, message, null);
                assertEquals("1", bar, 100);
            }

            @Override
            public void record() throws Exception {
                expect(message.getObjectProperty("bar")).andReturn(100);
            }
        }.doTest();
    }
}
