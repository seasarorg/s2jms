/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.Map;

import javax.jms.MapMessage;

import junit.framework.TestCase;

import org.seasar.framework.container.ExternalContext;
import org.seasar.jms.container.JMSRequest;
import org.seasar.jms.container.external.JMSExternalContext;
import org.seasar.jms.container.external.JMSRequestImpl;
import org.seasar.jms.core.mock.MapMessageMock;

/**
 * @author y-komori
 */
@SuppressWarnings("unchecked")
public class JMSExternalContextTest extends TestCase {

    ExternalContext externalContext;

    MapMessage message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        externalContext = new JMSExternalContext();
        message = new MapMessageMock();
    }

    /**
     * 
     */
    public void testSetRequest() {
        JMSRequest request = new JMSRequestImpl(message);
        externalContext.setRequest(request);

        Object dummy = new Object();
        try {
            externalContext.setRequest(dummy);
            fail();
        } catch (ClassCastException ex) {
        }
    }

    /**
     * 
     */
    public void testGetRequest() {
        JMSRequest request = new JMSRequestImpl(message);
        externalContext.setRequest(request);
        assertEquals(request, externalContext.getRequest());
    }

    /**
     * 
     */
    public void testSetResponce() {
        Object responce = new Object();
        externalContext.setResponse(responce);
    }

    /**
     * 
     */
    public void testGetResponce() {
        assertNull(externalContext.getResponse());
    }

    /**
     * 
     */
    public void testGetSession() {
        assertNull(externalContext.getSession());
    }

    /**
     * 
     */
    public void testSetApplication() {
        Object application = new Object();
        externalContext.setApplication(application);
    }

    /**
     * 
     */
    public void testGetApplication() {
        assertNull(externalContext.getApplication());
    }

    /**
     * 
     */
    public void testGetApplicationMap() {
        Map map = externalContext.getApplicationMap();
        assertTrue(map.isEmpty());
    }

    /**
     * 
     */
    public void testGetInitParameterMap() {
        Map map = externalContext.getInitParameterMap();
        assertTrue(map.isEmpty());
    }

    /**
     * 
     */
    public void testGetRequestCookieParameterMap() {
        Map map = externalContext.getRequestCookieMap();
        assertTrue(map.isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testGetRequestHeaderMap() throws Exception {
        message.setStringProperty("foo", "FOO");
        message.setStringProperty("bar", "BAR");
        JMSRequest request = new JMSRequestImpl(message);
        externalContext.setRequest(request);

        Map map = externalContext.getRequestHeaderMap();
        assertEquals(13, map.size());
        assertEquals("FOO", map.get("foo"));
        assertEquals("BAR", map.get("bar"));
    }

    /**
     * @throws Exception
     */
    public void testGetRequestHeaderValuesMap() throws Exception {
        message.setStringProperty("foo", "FOO");
        JMSRequest request = new JMSRequestImpl(message);
        externalContext.setRequest(request);

        Map map = externalContext.getRequestHeaderValuesMap();
        assertEquals(12, map.size());
        Object[] values = (Object[]) map.get("foo");
        assertEquals(1, values.length);
        assertEquals("FOO", values[0]);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void testGetRequestMap() {
        JMSRequest request = new JMSRequestImpl(message);
        request.setAttribute("foo", "FOO");
        request.setAttribute("bar", "BAR");
        externalContext.setRequest(request);

        Map map = externalContext.getRequestMap();
        assertEquals(2, map.size());

        map.put("baz", "BAZ");
        assertEquals("BAZ", request.getAttribute("baz"));
    }

    /**
     * @throws Exception
     */
    public void testGetRequestParameterMap() throws Exception {
        message.setString("foo", "FOO");
        message.setString("bar", "BAR");
        JMSRequest request = new JMSRequestImpl(message);
        externalContext.setRequest(request);

        Map map = externalContext.getRequestParameterMap();
        assertEquals(3, map.size());
        assertEquals("FOO", map.get("foo"));
        assertEquals("BAR", map.get("bar"));
    }

    /**
     * @throws Exception
     */
    public void testGetRequestParameterValuesMap() throws Exception {
        message.setString("foo", "FOO");
        JMSRequest request = new JMSRequestImpl(message);
        externalContext.setRequest(request);

        Map map = externalContext.getRequestParameterValuesMap();
        assertEquals(2, map.size());
        Object[] values = (Object[]) map.get("foo");
        assertEquals(1, values.length);
        assertEquals("FOO", values[0]);
    }

    /**
     * 
     */
    public void testGetSessionMap() {
        Map map = externalContext.getSessionMap();
        assertTrue(map.isEmpty());
    }

}
