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

import javax.jms.TextMessage;

import org.easymock.MockControl;
import org.seasar.jca.unit.EasyMockTestCase;

/**
 * @author Kenichiro Murata
 * 
 */
public class TextMessageBinderTest extends EasyMockTestCase {
    private TextMessageBinder binder;
    private TextMessage message;
    private MockControl messageControl;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        binder = new TextMessageBinder();
        messageControl = createStrictControl(TextMessage.class);
        message = (TextMessage) messageControl.getMock();
    }

    public TextMessageBinderTest(String name) {
        super(name);
    }

    public void testGetPeyLoad() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals("1", "TEST", binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                message.getText();
                messageControl.setReturnValue("TEST");
            }
        }.doTest();
    }

    public void testGetPeyLoadNull() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertNull("1", binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                message.getText();
                messageControl.setReturnValue(null);
            }
        }.doTest();
    }
}
