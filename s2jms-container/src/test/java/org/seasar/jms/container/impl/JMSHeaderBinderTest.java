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

public class JMSHeaderBinderTest extends EasyMockTestCase {
    Message message;
    String messageID;
    boolean JMSRedelivered;

    public JMSHeaderBinderTest() {
    }

    public JMSHeaderBinderTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        message = createStrictMock(Message.class);
        messageID = null;
        JMSRedelivered = false;
    }

    public void testMessageID() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                JMSHeaderBinder binder = new JMSHeaderBinder("messageID", BindingType.SHOULD,
                        JMSHeaderBinderTest.class.getDeclaredField("messageID"));
                binder.bind(JMSHeaderBinderTest.this, message, null);
                assertEquals("1", messageID, "abcdefg");
            }

            @Override
            public void record() throws Exception {
                expect(message.getJMSMessageID()).andReturn("abcdefg");
            }
        }.doTest();
    }

    public void testRedelivered() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                JMSHeaderBinder binder = new JMSHeaderBinder("JMSRedelivered", BindingType.SHOULD,
                        JMSHeaderBinderTest.class.getDeclaredField("JMSRedelivered"));
                binder.bind(JMSHeaderBinderTest.this, message, null);
                assertTrue("1", JMSRedelivered);
            }

            @Override
            public void record() throws Exception {
                expect(message.getJMSRedelivered()).andReturn(true);
            }
        }.doTest();
    }
}
