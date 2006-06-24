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
import javax.jms.Queue;
import javax.jms.Session;

import org.seasar.framework.unit.S2EasyMockTestCase;

/**
 * @author koichik
 */
public class JndiDestinationFactoryTest extends S2EasyMockTestCase {
    JndiDestinationFactory target;
    Session session;

    public JndiDestinationFactoryTest() {
    }

    public JndiDestinationFactoryTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getSimpleName() + ".dicon");
        target = new JndiDestinationFactory();
        session = createStrictMock(Session.class);
    }

    public void testCreateDestination() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                target.setName("jms.queue");
                Destination dest = target.getDestination(session);
                assertNotNull("1", dest);
                assertTrue("2", dest instanceof Queue);
            }

            @Override
            public void record() throws Exception {
            }
        }.doTest();
    }
}
