/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class ReplyToDestinationFactoryTest extends EasyMockTestCase {

    ReplyToDestinationFactory target;

    Session session;

    Message message;

    Destination destination;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new ReplyToDestinationFactory();
        session = createStrictMock(Session.class);
        message = createStrictMock(Message.class);
        destination = createStrictMock(Destination.class);
    }

    /**
     * @throws Exception
     */
    public void testCreateDestination() throws Exception {
        target.setMessage(message);
        Destination dest = target.getDestination(session);
        assertSame("1", destination, dest);
    }

    /**
     * @throws Exception
     */
    public void recordCreateDestination() throws Exception {
        expect(message.getJMSReplyTo()).andReturn(destination);
    }

}
