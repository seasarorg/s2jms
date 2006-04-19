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
        try {
            externalContext.setResponse(responce);
            fail();
        } catch (UnsupportedOperationException ex) {
        }

    }

    public void testGetResponce() {
        try {
            externalContext.getResponse();
            fail();
        } catch (UnsupportedOperationException ex) {
        }

    }

    public void testGetSession() {
        try {
            externalContext.getSession();
            fail();
        } catch (UnsupportedOperationException ex) {
        }

    }

    public void testSetApplication() {
        Object application = new Object();
        try {
            externalContext.setApplication(application);
            fail();
        } catch (UnsupportedOperationException ex) {
        }

    }

    public void testGetApplication() {
        try {
            externalContext.getApplication();
            fail();
        } catch (UnsupportedOperationException ex) {
        }

    }
}
