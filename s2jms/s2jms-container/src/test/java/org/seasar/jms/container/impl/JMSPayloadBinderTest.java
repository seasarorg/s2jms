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
package org.seasar.jms.container.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.annotation.tiger.BindingType;

public class JMSPayloadBinderTest extends TestCase {
    String foo;
    int bar;

    public JMSPayloadBinderTest() {
    }

    public JMSPayloadBinderTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        foo = null;
        bar = 0;
    }

    public void testFoo() throws Exception {
        JMSPayloadBinder binder = new JMSPayloadBinder("foo", BindingType.SHOULD,
                JMSPayloadBinderTest.class.getDeclaredField("foo"));
        binder.bind(JMSPayloadBinderTest.this, null, "FOO");
        assertEquals("1", foo, "FOO");
    }

    public void testBar() throws Exception {
        JMSPayloadBinder binder = new JMSPayloadBinder("bar", BindingType.SHOULD,
                JMSPayloadBinderTest.class.getDeclaredField("bar"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bar", 100);
        binder.bind(JMSPayloadBinderTest.this, null, map);
        assertEquals("1", bar, 100);
    }
}
