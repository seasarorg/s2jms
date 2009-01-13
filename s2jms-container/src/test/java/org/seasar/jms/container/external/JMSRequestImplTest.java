/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.jms.container.external;

import javax.jms.Message;

import junit.framework.TestCase;

import org.seasar.jms.container.JMSRequest;
import org.seasar.jms.core.mock.MessageMock;

/**
 * @author y-komori
 */
public class JMSRequestImplTest extends TestCase {

    JMSRequest request;

    Message message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        message = new MessageMock();
        request = new JMSRequestImpl(message);
    }

    /**
     * 
     */
    public void testSetAttribute() {
        Object foo = new Object();
        try {
            request.setAttribute("foo", foo);
        } catch (Exception ex) {
            fail("1");
        }
    }

    /**
     * 
     */
    public void testGetAttribute() {
        Object foo = new Object();
        Object bar = new Object();
        request.setAttribute("foo", foo);
        request.setAttribute("bar", bar);
        assertEquals(foo, request.getAttribute("foo"));
        assertEquals(bar, request.getAttribute("bar"));
    }

}
