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
package org.seasar.jms.container.external;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;

import org.seasar.framework.container.ExternalContext;
import org.seasar.jms.container.JMSRequest;

/**
 * @author y-komori
 * 
 */
public class JMSExternalContext implements ExternalContext {

    @SuppressWarnings("unchecked")
    private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap());

    private final ThreadLocal<JMSRequest> requests = new ThreadLocal<JMSRequest>();

    public JMSRequest getRequest() {
        return requests.get();
    }

    public void setRequest(final Object request) {
        requests.set(JMSRequest.class.cast(request));
    }

    public Object getResponse() {
        return null;
    }

    public void setResponse(final Object response) {
    }

    public Object getSession() {
        return null;
    }

    public Object getApplication() {
        return null;
    }

    public void setApplication(final Object application) {
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
        return new JMSRequestHeaderMap(getMessage());
    }

    public Map getRequestHeaderValuesMap() {
        return new JMSRequestHeaderValuesMap(getMessage());
    }

    public Map getRequestMap() {
        return JMSRequestImpl.class.cast(getRequest());
    }

    public Map getRequestParameterMap() {
        return new JMSRequestParameterMap(getMessage());
    }

    public Map getRequestParameterValuesMap() {
        return new JMSRequestParameterValuesMap(getMessage());
    }

    public Map getSessionMap() {
        return EMPTY_MAP;
    }

    protected Message getMessage() {
        return getRequest().getMessage();
    }

}
