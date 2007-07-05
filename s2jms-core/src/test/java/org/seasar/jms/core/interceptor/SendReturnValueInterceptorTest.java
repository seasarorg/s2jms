/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.jms.core.MessageSender;
import org.seasar.jms.core.message.impl.BytesMessageFactory;
import org.seasar.jms.core.message.impl.MapMessageFactory;
import org.seasar.jms.core.message.impl.ObjectMessageFactory;
import org.seasar.jms.core.message.impl.TextMessageFactory;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class SendReturnValueInterceptorTest extends S2TigerTestCase {

    SendReturnValueInterceptor interceptor;

    MessageSender sender;

    Function function;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getSimpleName() + ".dicon");

        sender = createStrictMock(MessageSender.class);
        S2Container container = getContainer().getChild(0);
        container.register(sender, "sender");
        container.init();
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testCreateMessageFactory() throws Exception {
        SendReturnValueInterceptor interceptor = new SendReturnValueInterceptor();

        TextMessageFactory textMessageFactory = (TextMessageFactory) interceptor
                .createMessageFactory("Test");
        assertEquals("1", "Test", textMessageFactory.getTextProvider().getText());

        BytesMessageFactory bytesMessageFactory = (BytesMessageFactory) interceptor
                .createMessageFactory(new byte[] { 1, 2, 3 });
        assertTrue("2", Arrays.equals(new byte[] { 1, 2, 3 }, bytesMessageFactory.getBytes()));

        Map map = new HashMap();
        MapMessageFactory mapMessageFactory = (MapMessageFactory) interceptor
                .createMessageFactory(map);
        assertEquals("3", map, mapMessageFactory.getMap());

        ObjectMessageFactory objectMessageFactory = (ObjectMessageFactory) interceptor
                .createMessageFactory(1);
        assertEquals("4", 1, objectMessageFactory.getObject());
    }

    /**
     * @throws Exception
     */
    public void testBytes() throws Exception {
        byte[] bytes = new byte[] { 1, 2, 3 };
        assertTrue("1", Arrays.equals(bytes, (byte[]) function.execute(bytes)));
    }

    /**
     * @throws Exception
     */
    public void recordBytes() throws Exception {
        sender.send(isA(BytesMessageFactory.class));
    }

    /**
     * @throws Exception
     */
    public void testMap() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("foo", "FOO");
        map.put("bar", "BAR");
        assertEquals("1", map, function.execute(map));
    }

    /**
     * @throws Exception
     */
    public void recordMap() throws Exception {
        sender.send(isA(MapMessageFactory.class));
    }

    /**
     * @throws Exception
     */
    public void testObject() throws Exception {
        assertEquals("1", 123, function.execute(123));
    }

    /**
     * @throws Exception
     */
    public void recordObject() throws Exception {
        sender.send(isA(ObjectMessageFactory.class));
    }

    /**
     * @throws Exception
     */
    public void testText() throws Exception {
        assertEquals("1", "Hoge", function.execute("Hoge"));
    }

    /**
     * @throws Exception
     */
    public void recordText() throws Exception {
        sender.send(isA(TextMessageFactory.class));
    }

    /**
     * @throws Exception
     */
    public void testNull() throws Exception {
        assertNull("1", function.execute(null));
    }

    interface Function {

        /**
         * @param arg
         * @return Object
         */
        Object execute(Object arg);
    }

    /**
     */
    public static class FunctionImpl implements Function {

        public Object execute(Object arg) {
            return arg;
        }
    }

}
