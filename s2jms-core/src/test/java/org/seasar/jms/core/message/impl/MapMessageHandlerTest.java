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

import java.util.Enumeration;
import java.util.Map;

import javax.jms.MapMessage;

import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class MapMessageHandlerTest extends EasyMockTestCase {

    MapMessageHandler target;

    MapMessage message;

    @SuppressWarnings("unchecked")
    Enumeration enumeration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new MapMessageHandler();
        message = createStrictMock(MapMessage.class);
        enumeration = createStrictMock(Enumeration.class);
    }

    /**
     * @throws Exception
     */
    public void testGetProperties() throws Exception {
        Map<String, Object> map = target.handleMessage(message);
        assertNotNull("1", map);
        assertEquals("2", 2, map.size());
        assertEquals("3", "FOO", map.get("foo"));
        assertEquals("4", "BAR", map.get("bar"));
    }

    /**
     * @throws Exception
     */
    public void recordGetProperties() throws Exception {
        expect(message.getMapNames()).andReturn(enumeration);
        expect(enumeration.hasMoreElements()).andReturn(true);
        expect(enumeration.nextElement()).andReturn("foo");
        expect(message.getObject("foo")).andReturn("FOO");
        expect(enumeration.hasMoreElements()).andReturn(true);
        expect(enumeration.nextElement()).andReturn("bar");
        expect(message.getObject("bar")).andReturn("BAR");
        expect(enumeration.hasMoreElements()).andReturn(false);
    }

}
