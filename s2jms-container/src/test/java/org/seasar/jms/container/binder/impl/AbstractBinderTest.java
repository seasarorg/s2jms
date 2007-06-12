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
package org.seasar.jms.container.binder.impl;

import javax.jms.Message;

import junit.framework.TestCase;

import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.container.binder.impl.AbstractBinder;
import org.seasar.jms.container.exception.NotBoundException;

/**
 * @author koichik
 */
public class AbstractBinderTest extends TestCase {

    boolean called;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        called = false;
    }

    /**
     * @throws Exception
     */
    public void testBind() throws Exception {
        AbstractBinder binder = new AbstractBinder(null, null, null) {

            @Override
            protected boolean doBind(Object target, Message message, Object payload) {
                called = true;
                return true;
            }
        };
        binder.bind(null, null, null);
        assertTrue("1", called);
    }

    /**
     * @throws Exception
     */
    public void testMust() throws Exception {
        TestBinder binder = new TestBinder("bar", BindingType.MUST);
        try {
            binder.bind("", null, null);
            fail("1");
        } catch (NotBoundException expected) {
            System.out.println(expected);
        }
        assertTrue("2", called);
    }

    /**
     * @throws Exception
     */
    public void testShould() throws Exception {
        TestBinder binder = new TestBinder("bar", BindingType.SHOULD);
        binder.bind("", null, null);
        assertTrue("1", called);
    }

    /**
     * @throws Exception
     */
    public void testMay() throws Exception {
        TestBinder binder = new TestBinder("bar", BindingType.MAY);
        binder.bind(null, null, null);
        assertTrue("1", called);
    }

    /**
     * @author koichik
     * 
     */
    public class TestBinder extends AbstractBinder {

        /**
         * @param name
         * @param bindingType
         */
        public TestBinder(String name, BindingType bindingType) {
            super(name, bindingType, null);
        }

        @Override
        protected boolean doBind(Object target, Message message, Object payload) {
            called = true;
            return false;
        }

    }

}
