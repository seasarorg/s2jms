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
import javax.jms.JMSException;
import javax.jms.Session;

import org.seasar.framework.unit.EasyMockTestCase;
import org.seasar.jms.core.destination.DestinationFactory;

/**
 * @author koichik
 */
public class AbstractDestinationFactoryTest extends EasyMockTestCase {
    DestinationFactory target;
    Session session;
    Destination destination;
    int count;

    public AbstractDestinationFactoryTest() {
    }

    public AbstractDestinationFactoryTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        session = createStrictMock(Session.class);
        destination = createStrictMock(Destination.class);
        count = 0;
    }

    public void testGetDestination() throws Exception {
        target = new AbstractDestinationFactory() {
            @Override
            protected Destination createDestination(Session session) throws JMSException {
                ++count;
                return AbstractDestinationFactoryTest.this.destination;
            }
        };

        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals("1", destination, target.getDestination(session));
                assertEquals("2", 1, count);
                assertEquals("3", destination, target.getDestination(session));
                assertEquals("4", 1, count);
            }

            @Override
            public void record() throws Exception {
            }
        }.doTest();
    }
}
