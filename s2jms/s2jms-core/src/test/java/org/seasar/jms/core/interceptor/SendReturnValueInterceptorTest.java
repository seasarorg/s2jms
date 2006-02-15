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
/**
 * 
 */
package org.seasar.jms.core.interceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.jms.core.message.impl.BytesMessageFactory;
import org.seasar.jms.core.message.impl.MapMessageFactory;
import org.seasar.jms.core.message.impl.ObjectMessageFactory;
import org.seasar.jms.core.message.impl.TextMessageFactory;

/**
 * @author koichik
 * 
 */
public class SendReturnValueInterceptorTest extends TestCase {

    public SendReturnValueInterceptorTest() {
    }

    public SendReturnValueInterceptorTest(String name) {
        super(name);
    }

    public void testCreateMessageFactory() throws Exception {
        SendReturnValueInterceptor interceptor = new SendReturnValueInterceptor();

        TextMessageFactory textMessageFactory = (TextMessageFactory) interceptor
                .createMessageFactory("Test", String.class);
        assertEquals("1", "Test", textMessageFactory.getTextProvider().getText());

        BytesMessageFactory bytesMessageFactory = (BytesMessageFactory) interceptor
                .createMessageFactory(new byte[] { 1, 2, 3 }, byte[].class);
        assertTrue("2", Arrays.equals(new byte[] { 1, 2, 3 }, bytesMessageFactory.getBytes()));

        Map map = new HashMap();
        MapMessageFactory mapMessageFactory = (MapMessageFactory) interceptor
                .createMessageFactory(map, Map.class);
        assertEquals("3", map, mapMessageFactory.getMap());

        ObjectMessageFactory objectMessageFactory = (ObjectMessageFactory) interceptor
                .createMessageFactory(1, Integer.class);
        assertEquals("4", 1, objectMessageFactory.getObject());
    }
}
