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
package org.seasar.jms.container.binder.impl;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jms.container.binder.BindingSupport;
import org.seasar.jms.container.binder.impl.PropertyBindingSupport;

/**
 * @author koichik
 * 
 */
public class PropertyBindingSupportTest extends TestCase {

    BeanDesc beanDesc = BeanDescFactory.getBeanDesc(getClass());

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
        BindingSupport bind1 = new PropertyBindingSupport(beanDesc.getPropertyDesc("f1"));
        bind1.bind(this, 100);
        assertEquals("1", 100, f1);

        BindingSupport bind2 = new PropertyBindingSupport(beanDesc.getPropertyDesc("f2"));
        bind2.bind(this, "Hoge");
        assertEquals("2", "Hoge", f2);
    }

    /**
     * @param f1
     */
    public void setF1(int f1) {
        this.f1 = f1;
    }

    /**
     * @param f2
     */
    public void setF2(String f2) {
        this.f2 = f2;
    }

}
