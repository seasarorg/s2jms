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

import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.ExternalContext;
import org.seasar.jms.container.JMSRequest;

/**
 * @author y-komori
 * 
 */
public class JMSExternalContextTest extends TestCase {
    ExternalContext externalContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        externalContext = new JMSExternalContext();
    }

    public void testSetRequest() {
        JMSRequest request = new JMSRequestImpl();
        try {
            externalContext.setRequest(request);
        } catch (Exception ex) {
            fail("1");
        }

        Object dummy = new Object();
        try {
            externalContext.setRequest(dummy);
            fail("2");
        } catch (ClassCastException ex) {
        }
    }

    public void testGetRequest() {
        JMSRequest request = new JMSRequestImpl();
        externalContext.setRequest(request);
        assertEquals(request, externalContext.getRequest());
    }

    public void testSetResponce() {
        Object responce = new Object();
        externalContext.setResponse(responce);
    }

    public void testGetResponce() {
        assertNull(externalContext.getResponse());
    }

    public void testGetSession() {
        assertNull(externalContext.getSession());
    }

    public void testSetApplication() {
        Object application = new Object();
        externalContext.setApplication(application);
    }

    public void testGetApplication() {
        assertNull(externalContext.getApplication());
    }

    public void testGetApplicationMap() {
        Map map = externalContext.getApplicationMap();
        assertTrue(map.isEmpty());
    }

    public void testGetInitParameterMap() {
        Map map = externalContext.getInitParameterMap();
        assertTrue(map.isEmpty());
    }

    public void testGetRequestCookieParameterMap() {
        Map map = externalContext.getRequestCookieMap();
        assertTrue(map.isEmpty());
    }

    public void testGetRequestHeaderMap() {
        Map map = externalContext.getRequestHeaderMap();
        assertTrue(map.isEmpty());
    }

    public void testGetRequestHeaderValuesMap() {
        Map map = externalContext.getRequestHeaderValuesMap();
        assertTrue(map.isEmpty());
    }

    public void testGetRequestMap() {
        Map map = externalContext.getRequestMap();
        assertTrue(map.isEmpty());
    }

    public void testGetRequestParameterMap() {
        Map map = externalContext.getRequestParameterMap();
        assertTrue(map.isEmpty());
    }

    public void testGetRequestParameterValuesMap() {
        Map map = externalContext.getRequestParameterValuesMap();
        assertTrue(map.isEmpty());
    }

    public void testGetSessionMap() {
        Map map = externalContext.getSessionMap();
        assertTrue(map.isEmpty());
    }

}
