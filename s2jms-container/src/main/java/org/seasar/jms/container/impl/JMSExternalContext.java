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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.ExternalContext;
import org.seasar.jms.container.JMSRequest;

/**
 * @author y-komori
 * 
 */
public class JMSExternalContext implements ExternalContext {
    @SuppressWarnings("unchecked")
    private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap());

    private ThreadLocal<JMSRequest> requests = new ThreadLocal<JMSRequest>();

    public Object getRequest() {
        return requests.get();
    }

    public void setRequest(Object request) {
        requests.set(JMSRequest.class.cast(request));
    }

    public Object getResponse() {
        return null;
    }

    public void setResponse(Object response) {
    }

    public Object getSession() {
        return null;
    }

    public Object getApplication() {
        return null;
    }

    public void setApplication(Object application) {
    }

    public Map getApplicationMap() {
        return EMPTY_MAP;
    }

    public Map getInitParameterMap() {
        return EMPTY_MAP;
    }

    public Map getRequestCookieMap() {
        return EMPTY_MAP;
    }

    public Map getRequestHeaderMap() {
        return EMPTY_MAP;
    }

    public Map getRequestHeaderValuesMap() {
        return EMPTY_MAP;
    }

    public Map getRequestMap() {
        return EMPTY_MAP;
    }

    public Map getRequestParameterMap() {
        return EMPTY_MAP;
    }

    public Map getRequestParameterValuesMap() {
        return EMPTY_MAP;
    }

    public Map getSessionMap() {
        return EMPTY_MAP;
    }

}
