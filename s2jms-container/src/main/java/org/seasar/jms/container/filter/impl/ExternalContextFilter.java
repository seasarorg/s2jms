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

package org.seasar.jms.container.filter.impl;

import javax.jms.Message;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.container.JMSRequest;
import org.seasar.jms.container.external.JMSRequestImpl;
import org.seasar.jms.container.filter.Filter;
import org.seasar.jms.container.filter.FilterChain;

/**
 * @author koichik
 * 
 */
public class ExternalContextFilter implements Filter {

    @Binding(bindingType = BindingType.MUST)
    protected S2Container container;

    public void doFilter(final Message message, final FilterChain chain) throws Exception {
        final ExternalContext externalContext = container.getRoot().getExternalContext();
        setRequest(externalContext, new JMSRequestImpl(message));
        try {
            chain.doFilter(message);
        } finally {
            setRequest(externalContext, null);
        }
    }

    protected void setRequest(final ExternalContext externalContext, final JMSRequest request) {
        if (externalContext != null) {
            externalContext.setRequest(request);
        }
    }

}
