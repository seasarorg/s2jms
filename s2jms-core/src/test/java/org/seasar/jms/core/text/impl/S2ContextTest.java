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
package org.seasar.jms.core.text.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author bowez
 */
public class S2ContextTest extends TestCase {

    /**
     * Test method for 'org.seasar.jms.core.text.impl.S2Context.put(String,
     * Object)'
     */
    public void testPut() {
        S2Context context = new S2Context();
        try {
            context.put("foo", "bar");
            fail();
        } catch (final UnsupportedOperationException e) {
        }
    }

    /**
     * Test method for 'org.seasar.jms.core.text.impl.S2Context.get(String)'
     */
    public void testGet() {
        S2Container container = new S2ContainerImpl();
        container.register("Hoge", "hoge");
        container.register("FOO", "foo");
        container.register("BAR", "bar");
        container.init();

        S2Context context = new S2Context();
        context.setContainer(container);
        assertEquals("Hoge", context.get("hoge"));
        assertEquals("FOO", context.get("foo"));
        assertEquals("BAR", context.get("bar"));

        assertNull(context.get("piyo"));
    }

    /**
     * Test method for
     * 'org.seasar.jms.core.text.impl.S2Context.containsKey(Object)'
     */
    public void testContainsKey() {
        S2Container container = new S2ContainerImpl();
        container.register("Hoge", "hoge");
        container.register("FOO", "foo");
        container.register("BAR", "bar");
        container.init();

        S2Context context = new S2Context();
        context.setContainer(container);
        assertTrue(context.containsKey("hoge"));
        assertTrue(context.containsKey("foo"));
        assertTrue(context.containsKey("bar"));

        assertFalse(context.containsKey("piyo"));
    }

    /**
     * Test method for 'org.seasar.jms.core.text.impl.S2Context.getKeys()'
     */
    public void testGetKeys() {
        S2Context context = new S2Context();
        try {
            context.getKeys();
            fail();
        } catch (final UnsupportedOperationException e) {
        }
    }

    /**
     * Test method for 'org.seasar.jms.core.text.impl.S2Context.remove(Object)'
     */
    public void testRemove() {
        S2Context context = new S2Context();
        try {
            context.remove("foo");
            fail();
        } catch (final UnsupportedOperationException e) {
        }
    }

}
