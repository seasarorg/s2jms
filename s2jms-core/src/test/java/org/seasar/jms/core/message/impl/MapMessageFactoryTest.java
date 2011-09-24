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
package org.seasar.jms.core.message.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Session;

import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class MapMessageFactoryTest extends EasyMockTestCase {

    MapMessageFactory target;

    Session session;

    MapMessage message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new MapMessageFactory();
        session = createStrictMock(Session.class);
        message = createStrictMock(MapMessage.class);
    }

    /**
     * @throws Exception
     */
    public void testMapMessage() throws Exception {
        target.setCorrelationID("id");
        target.addProperty("foo", "FOO");
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("hoge", new Integer(1));
        map.put("hogehoge", "HogeHoge");
        target.setMap(map);
        assertSame("1", message, target.createMessage(session));
    }

    /**
     * @throws Exception
     */
    public void recordMapMessage() throws Exception {
        expect(session.createMapMessage()).andReturn(message);
        message.setJMSCorrelationID("id");
        message.setObjectProperty("foo", "FOO");
        message.setObject("hoge", new Integer(1));
        message.setObject("hogehoge", "HogeHoge");
    }
}
