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
package org.seasar.jms.core.message.impl;

import javax.jms.Session;
import javax.jms.TextMessage;

import org.seasar.framework.unit.EasyMockTestCase;
import org.seasar.jms.core.text.impl.TextHolder;

import static org.easymock.EasyMock.expect;

/**
 * @author koichik
 */
public class TextMessageFactoryTest extends EasyMockTestCase {
    TextMessageFactory target;
    Session session;
    TextMessage message;

    public TextMessageFactoryTest() {
    }

    public TextMessageFactoryTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new TextMessageFactory();
        session = createStrictMock(Session.class);
        message = createStrictMock(TextMessage.class);
    }

    public void test() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                target.setCorrelationID("id");
                target.addProperty("foo", "FOO");
                target.setTextProvider(new TextHolder("Hoge"));
                assertSame("1", message, target.createMessage(session));
            }

            @Override
            public void record() throws Exception {
                expect(session.createTextMessage()).andReturn(message);
                message.setJMSCorrelationID("id");
                message.setObjectProperty("foo", "FOO");
                message.setText("Hoge");
            }
        }.doTest();
    }

}