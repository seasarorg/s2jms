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
package org.seasar.jms.container.binder.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.container.binder.impl.JMSBodyBinder;

/**
 * @author koichik
 * 
 */
public class JMSBodyBinderTest extends TestCase {

    String foo;

    int bar;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        foo = null;
        bar = 0;
    }

    /**
     * @throws Exception
     */
    public void testFoo() throws Exception {
        JMSBodyBinder binder = new JMSBodyBinder("foo", BindingType.SHOULD,
                JMSBodyBinderTest.class.getDeclaredField("foo"));
        binder.bind(JMSBodyBinderTest.this, null, "FOO");
        assertEquals("1", foo, "FOO");
    }

    /**
     * @throws Exception
     */
    public void testBar() throws Exception {
        JMSBodyBinder binder = new JMSBodyBinder("bar", BindingType.SHOULD,
                JMSBodyBinderTest.class.getDeclaredField("bar"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bar", 100);
        binder.bind(JMSBodyBinderTest.this, null, map);
        assertEquals("1", bar, 100);
    }

}
