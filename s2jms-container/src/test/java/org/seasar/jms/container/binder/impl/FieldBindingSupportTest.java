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

import org.seasar.jms.container.binder.BindingSupport;
import org.seasar.jms.container.binder.impl.FieldBindingSupport;

import junit.framework.TestCase;


/**
 * @author koichik
 * 
 */
public class FieldBindingSupportTest extends TestCase {

    int f1;

    String f2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        f1 = 0;
        f2 = null;
    }

    /**
     * @throws Exception
     */
    public void testBind() throws Exception {
        BindingSupport bind1 = new FieldBindingSupport(getClass().getDeclaredField("f1"));
        bind1.bind(this, 100);
        assertEquals("1", 100, f1);

        BindingSupport bind2 = new FieldBindingSupport(getClass().getDeclaredField("f2"));
        bind2.bind(this, "Hoge");
        assertEquals("2", "Hoge", f2);
    }

}
