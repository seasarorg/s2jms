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
import javax.jms.Session;

import org.seasar.framework.unit.EasyMockTestCase;

/**
 * @author koichik
 */
public class SimpleDestinationFactoryTest extends EasyMockTestCase {
    SimpleDestinationFactory target;
    Session session;
    Destination destination;

    public SimpleDestinationFactoryTest() {
    }

    public SimpleDestinationFactoryTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new SimpleDestinationFactory();
        session = createStrictMock(Session.class);
        destination = createMock(Destination.class);
    }

    public void testCreateDestination() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                target.setDestination(destination);
                assertSame("1", destination, target.getDestination(null));
            }

            @Override
            public void record() throws Exception {
            }
        }.doTest();
    }
}
