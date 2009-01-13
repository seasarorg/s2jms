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

import java.util.Collections;
import java.util.Map;

import javax.jms.Message;

import org.seasar.framework.container.ExternalContext;
import org.seasar.jms.container.JMSRequest;

/**
 * JMSメッセージを外部コンテキストとして扱うコンポーネントです。
 * 
 * @author y-komori
 */
public class JMSExternalContext implements ExternalContext {

    // instance fields
    /** 現在の{@link JMSRequest}を保持するスレッドローカル */
    protected final ThreadLocal<JMSRequest> requests = new ThreadLocal<JMSRequest>();

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

    @SuppressWarnings("unchecked")
    public Map getApplicationMap() {
        return Collections.EMPTY_MAP;
    }

    @SuppressWarnings("unchecked")
    public Map getInitParameterMap() {
        return Collections.EMPTY_MAP;
    }

    @SuppressWarnings("unchecked")
    public Map getRequestCookieMap() {
        return Collections.EMPTY_MAP;
    }

    @SuppressWarnings("unchecked")
    public Map getRequestHeaderMap() {
        return new JMSRequestHeaderMap(getMessage());
    }

    @SuppressWarnings("unchecked")
    public Map getRequestHeaderValuesMap() {
        return new JMSRequestHeaderValuesMap(getMessage());
    }

    @SuppressWarnings("unchecked")
    public Map getRequestMap() {
        return JMSRequestImpl.class.cast(getRequest());
    }

    @SuppressWarnings("unchecked")
    public Map getRequestParameterMap() {
        return new JMSRequestParameterMap(getMessage());
    }

    @SuppressWarnings("unchecked")
    public Map getRequestParameterValuesMap() {
        return new JMSRequestParameterValuesMap(getMessage());
    }

    @SuppressWarnings("unchecked")
    public Map getSessionMap() {
        return Collections.EMPTY_MAP;
    }

    /**
     * 現在のスレッドに関連づけられた{@link Message}を返します。
     * 
     * @return 現在のスレッドに関連づけられた{@link Message}
     */
    protected Message getMessage() {
        return getRequest().getMessage();
    }

}
