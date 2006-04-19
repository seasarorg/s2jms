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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Session;

import org.seasar.jca.unit.EasyMockTestCase;

import static org.easymock.EasyMock.expect;

/**
 * @author koichik
 */
public class MapMessageFactoryTest extends EasyMockTestCase {
    MapMessageFactory target;
    Session session;
    MapMessage message;

    public MapMessageFactoryTest() {
    }

    public MapMessageFactoryTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new MapMessageFactory();
        session = createStrictMock(Session.class);
        message = createStrictMock(MapMessage.class);
    }

    public void test() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                target.setCorrelationId("id");
                target.addProperty("foo", "FOO");
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("hoge", new Integer(1));
                map.put("hogehoge", "HogeHoge");
                target.setMap(map);
                assertSame("1", message, target.createMessage(session));
            }

            @Override
            public void verify() throws Exception {
                expect(session.createMapMessage()).andReturn(message);
                message.setJMSCorrelationID("id");
                message.setObjectProperty("foo", "FOO");
                message.setObject("hoge", new Integer(1));
                message.setObject("hogehoge", "HogeHoge");
            }
        }.doTest();
    }
}
