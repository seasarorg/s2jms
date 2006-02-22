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
package org.seasar.jms.core.destination.impl;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Session;

import org.seasar.jca.unit.EasyMockTestCase;

import static org.easymock.EasyMock.expect;

/**
 * @author koichik
 */
public class ReplyToDestinationFactoryTest extends EasyMockTestCase {
    ReplyToDestinationFactory target;
    Session session;
    Message message;
    Destination destination;

    public ReplyToDestinationFactoryTest() {
    }

    public ReplyToDestinationFactoryTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new ReplyToDestinationFactory();
        session = createStrictMock(Session.class);
        message = createStrictMock(Message.class);
        destination = createStrictMock(Destination.class);
    }

    public void testCreateDestination() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                target.setMessage(message);
                Destination dest = target.getDestination(session);
                assertSame("1", destination, dest);
            }

            @Override
            public void verify() throws Exception {
                expect(message.getJMSReplyTo()).andReturn(destination);
            }
        }.doTest();
    }
}
