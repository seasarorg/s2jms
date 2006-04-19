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
package org.seasar.jms.connector.support;

import javax.jms.JMSException;

import org.seasar.jms.core.exception.SJMSException;

/**
 * @author koichik
 */
public class ExceptionUtil {
    public static JMSException toJMSException(final Throwable t) {
        for (Throwable e = t; e.getCause() != null; e = e.getCause()) {
            if (e instanceof JMSException) {
                return (JMSException) e;
            }
        }
        return new SJMSException("EJCA0000", t);
    }
}
