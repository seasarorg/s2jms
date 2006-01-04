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
package org.seasar.jms.connector.mcf;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.security.auth.Subject;

import org.seasar.jca.cm.ConnectionManagerImpl;
import org.seasar.jca.exception.SResourceException;
import org.seasar.jms.connector.JMSManagedConnection;
import org.seasar.jms.connector.ra.JMSResourceAdapter;
import org.seasar.jms.connector.support.ConnectionRequestInfoImpl;
import org.seasar.jndi.JndiContextFactory;

/**
 * @author koichik
 */
public abstract class AbstractManagedConnectionFactory implements ManagedConnectionFactory,
        ResourceAdapterAssociation {
    protected PrintWriter out;
    protected JMSResourceAdapter ra;
    protected Hashtable<?, ?> environment = JndiContextFactory.ENVIRONMENT;
    protected String connectionFactoryName;
    protected String user;
    protected String password;
    protected ConnectionFactory cf;

    public Object createConnectionFactory() throws ResourceException {
        return createConnectionFactory(new ConnectionManagerImpl());
    }

    public ManagedConnection createManagedConnection(final Subject subject,
            final ConnectionRequestInfo info) throws ResourceException {
        try {
            return createManagedConnection((ConnectionRequestInfoImpl) info);
        } catch (final JMSException e) {
            throw new SResourceException("EJMS0000", e);
        }
    }

    protected abstract ManagedConnection createManagedConnection(ConnectionRequestInfoImpl info)
            throws ResourceException, JMSException;

    protected Object getConnectionFactory() throws ResourceException {
        try {
            if (cf == null) {
                final Context ctx = new InitialContext(environment);
                try {
                    cf = (ConnectionFactory) ctx.lookup(getConnectionFactoryName());
                } finally {
                    ctx.close();
                }
            }
            return cf;
        } catch (final NamingException e) {
            throw new SResourceException("EJCA0000", e);
        }
    }

    public ManagedConnection matchManagedConnections(final Set mcSet, final Subject subject,
            final ConnectionRequestInfo info) throws ResourceException {
        for (final Iterator it = mcSet.iterator(); it.hasNext();) {
            final JMSManagedConnection mc = (JMSManagedConnection) it.next();
            if (mc.match(info)) {
                return (ManagedConnection) mc;
            }
        }
        return null;
    }

    public PrintWriter getLogWriter() {
        return out;
    }

    public void setLogWriter(final PrintWriter out) throws ResourceException {
        this.out = out;
    }

    public ResourceAdapter getResourceAdapter() {
        return ra;
    }

    public void setResourceAdapter(final ResourceAdapter ra) throws ResourceException {
        this.ra = (JMSResourceAdapter) ra;
    }

    public Hashtable<?, ?> getEnvironment() {
        return environment;
    }

    public void setEnvironment(final Hashtable<?, ?> environment) {
        this.environment = environment;
    }

    public String getConnectionFactoryName() {
        if (connectionFactoryName != null) {
            return connectionFactoryName;
        }
        return ra.getConnectionFactoryName();
    }

    public void setConnectionFactoryName(final String connectionFactoryName) {
        this.connectionFactoryName = connectionFactoryName;
    }

    public String getUser() {
        if (user != null) {
            return user;
        }
        return ra.getConnectionFactoryName();
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getPassword() {
        if (password != null) {
            return password;
        }
        return ra.getPassword();
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
