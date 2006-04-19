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
package org.seasar.jms.connector.cf;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

import org.seasar.jms.connector.support.ConnectionRequestInfoImpl;
import org.seasar.jms.connector.support.ExceptionUtil;

/**
 * @author koichik
 */
public class ConnectionFactoryImpl implements ConnectionFactory {
    protected final ManagedConnectionFactory mcf;
    protected final ConnectionManager cm;

    public ConnectionFactoryImpl(final ManagedConnectionFactory mcf, final ConnectionManager cm) {
        this.mcf = mcf;
        this.cm = cm;
    }

    public Connection createConnection() throws JMSException {
        try {
            return (Connection) cm.allocateConnection(mcf, null);
        } catch (final ResourceException e) {
            throw ExceptionUtil.toJMSException(e);
        }
    }

    public Connection createConnection(final String user, final String password)
            throws JMSException {
        try {
            return (Connection) cm.allocateConnection(mcf, new ConnectionRequestInfoImpl(user,
                    password));
        } catch (final ResourceException e) {
            throw ExceptionUtil.toJMSException(e);
        }
    }
}
