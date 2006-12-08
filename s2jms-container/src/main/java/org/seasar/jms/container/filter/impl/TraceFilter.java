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

import org.seasar.framework.log.Logger;
import org.seasar.jms.container.filter.Filter;
import org.seasar.jms.container.filter.FilterChain;
import org.seasar.jms.container.impl.JMSContainerImpl;

/**
 * @author koichik
 * 
 */
public class TraceFilter implements Filter {

    private static final Logger logger = Logger.getLogger(JMSContainerImpl.class);

    public void doFilter(final Message message, final FilterChain chain) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.log("DJMS-CONTAINER2100", new Object[0]);
        }
        try {
            chain.doFilter(message);
        } catch (final Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.log("EJMS-CONTAINER2102", new Object[0], e);
            }
            throw e;
        } finally {
            if (logger.isDebugEnabled()) {
                logger.log("DJMS-CONTAINER2101", new Object[0]);
            }
        }
    }

}
