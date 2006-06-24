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

import org.seasar.jms.container.JMSRequest;

/**
 * @author y-komori
 * 
 */
public class JMSRequestImpl implements JMSRequest {
    @SuppressWarnings("unchecked")
    protected static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap());

    protected final Map<String, Object> attributeMap = new HashMap<String, Object>();

    /**
     * @see org.seasar.jms.container.JMSRequest#getAttribute(java.lang.String)
     */
    public Object getAttribute(final String name) {
        return attributeMap.get(name);
    }

    /**
     * @see org.seasar.jms.container.JMSRequest#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(final String name, final Object component) {
        attributeMap.put(name, component);
    }

    public Map getRequestMap() {
        return attributeMap;
    }

    public Map getHeaderMap() {
        return EMPTY_MAP;
    }

    public Map getHeaderValuesMap() {
        return EMPTY_MAP;
    }

    public Map getParameterMap() {
        return EMPTY_MAP;
    }

    public Map getParameterValuesMap() {
        return EMPTY_MAP;
    }

}