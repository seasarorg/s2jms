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

import org.seasar.framework.container.ExternalContext;
import org.seasar.jms.container.JMSRequest;

/**
 * @author y-komori
 *
 */
public class JMSExternalContext implements ExternalContext {
    private ThreadLocal<JMSRequest> requests = new ThreadLocal<JMSRequest>();

    public Object getRequest() {
        return requests.get();
    }

    public void setRequest(Object request) {
        requests.set(JMSRequest.class.cast(request));
    }

    public Object getResponse() {
        throw new UnsupportedOperationException("getResponce");
    }

    public void setResponse(Object response) {
        throw new UnsupportedOperationException("setResponce");
    }

    public Object getSession() {
        throw new UnsupportedOperationException("getSession");
    }
    
    public Object getApplication() {
        throw new UnsupportedOperationException("getApplication");
    }

    public void setApplication(Object application) {
        throw new UnsupportedOperationException("setApplication");
    }
}
