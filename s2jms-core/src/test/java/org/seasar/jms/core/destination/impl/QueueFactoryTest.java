/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import javax.jms.Queue;
import javax.jms.Session;

import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class QueueFactoryTest extends EasyMockTestCase {

    QueueFactory target;

    Session session;

    Queue queue;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new QueueFactory();
        session = createStrictMock(Session.class);
        queue = createStrictMock(Queue.class);
    }

    /**
     * @throws Exception
     */
    public void testCreateDestination() throws Exception {
        target.setName("hoge");
        Destination dest = target.getDestination(session);
        assertSame("1", queue, dest);
    }

    /**
     * @throws Exception
     */
    public void recordCreateDestination() throws Exception {
        expect(session.createQueue("hoge")).andReturn(queue);
    }

}
