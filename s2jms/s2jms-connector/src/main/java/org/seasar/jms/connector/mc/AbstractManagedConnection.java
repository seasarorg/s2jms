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
package org.seasar.jms.connector.mc;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.seasar.framework.log.Logger;
import org.seasar.jca.exception.SResourceException;
import org.seasar.jms.connector.ConnectionHandle;
import org.seasar.jms.connector.JMSManagedConnection;
import org.seasar.jms.core.exception.SJMSException;

/**
 * @author koichik
 */
public abstract class AbstractManagedConnection implements ManagedConnection, JMSManagedConnection,
        ExceptionListener {
    private static final Logger logger = Logger.getLogger(AbstractManagedConnection.class);

    protected ConnectionRequestInfo info;
    protected ManagedConnectionFactory mcf;
    protected Connection pc;
    protected Session ps;
    protected XAResource xa;
    protected ConnectionHandle activeCh;
    protected final Set<ConnectionHandle> connectionHandles = new HashSet<ConnectionHandle>();
    protected final Set<ConnectionEventListener> listeners = new LinkedHashSet<ConnectionEventListener>();
    protected PrintWriter out;

    protected AbstractManagedConnection(final ConnectionRequestInfo info,
            final ManagedConnectionFactory mcf, final Connection pc) throws ResourceException {
        try {
            this.info = info;
            this.mcf = mcf;
            this.pc = pc;
            pc.setExceptionListener(this);
        } catch (JMSException e) {
            throw new SResourceException("EJMS0000", e);
        }
    }

    public abstract ConnectionHandle createConnectionHandle() throws ResourceException;

    protected abstract Session createPhysicalSession(boolean transacted, int acknowledgeMode)
            throws JMSException;

    public boolean match(final ConnectionRequestInfo info) {
        return this.info == info || this.info.equals(info);
    }

    public Object getConnection(final Subject subject, final ConnectionRequestInfo info)
            throws ResourceException {
        assertPhysicalConnectionOpened();
        assertConnectionHandleClosed();

        try {
            final ConnectionHandle ch = createConnectionHandle();
            ch.associateConnection(this, pc);
            connectionHandles.add(ch);
            activeCh = ch;
        } catch (JMSException e) {
            throw new SResourceException("EJMS0000", e);
        }
        return activeCh;
    }

    public void associateConnection(final Object connection) throws ResourceException {
        assertPhysicalConnectionOpened();
        try {
            if (!(connection instanceof ConnectionHandle)) {
                throw new SResourceException("");
            }

            final ConnectionHandle ch = (ConnectionHandle) connection;
            final AbstractManagedConnection mc = (AbstractManagedConnection) ch
                    .getManagedConnection();
            if (mc.mcf != mcf) {
                throw new SResourceException("");
            }

            mc.connectionHandles.remove(ch);
            connectionHandles.add(ch);
            if (activeCh != null) {
                activeCh.setActive(false);
            }
            ch.associateConnection(this, pc);
            activeCh = ch;
        } catch (final JMSException e) {
            throw new SResourceException("EJMS0000", e);
        }
    }

    public Session getPhysicalSession(final boolean transacted, final int acknowledgeMode)
            throws JMSException {
        if (pc == null) {
            throw new SJMSException("");
        }
        if (ps == null) {
            ps = createPhysicalSession(transacted, acknowledgeMode);
        }
        return ps;
    }

    public void sessionHandleClosed(final ConnectionHandle ch) throws JMSException {
        if (pc == null || ch != activeCh || isManagedTx()) {
            return;
        }

        try {
            ps.close();
        } finally {
            ps = null;
        }
    }

    public void connectionHandleClosed(final ConnectionHandle ch) throws JMSException {
        if (pc == null || ch != activeCh) {
            return;
        }

        connectionHandles.remove(ch);
        activeCh = null;
        fireConnectionClosed();
    }

    public void cleanup() throws ResourceException {
        if (activeCh != null) {
            activeCh.cleanup();
            connectionHandles.clear();
            activeCh = null;
        }

        xa = null;
        if (ps != null) {
            try {
                ps.close();
            } catch (final JMSException e) {
                logger.log("EJMS0000", null, e);
            } finally {
                ps = null;
            }
        }
    }

    public void destroy() throws ResourceException {
        cleanup();
        if (pc != null) {
            try {
                pc.close();
            } catch (final JMSException e) {
                logger.log("EJMS0000", null, e);
            }
        }
    }

    public void onException(final JMSException e) {
        logger.log("EJMS0000", null, e);
        if (activeCh != null) {
            activeCh.onException(e);
        }
        fireConnectionErrorOccurred(e);
    }

    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    public void addConnectionEventListener(final ConnectionEventListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeConnectionEventListener(final ConnectionEventListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public PrintWriter getLogWriter() throws ResourceException {
        return out;
    }

    public void setLogWriter(final PrintWriter out) throws ResourceException {
        this.out = out;
    }

    protected void fireConnectionClosed() {
        final ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
        synchronized (listeners) {
            final Iterator it = listeners.iterator();
            while (it.hasNext()) {
                final ConnectionEventListener listener = (ConnectionEventListener) it.next();
                listener.connectionClosed(event);
            }
        }
    }

    protected void fireConnectionErrorOccurred(final Exception e) {
        final ConnectionEvent event = new ConnectionEvent(this,
                ConnectionEvent.CONNECTION_ERROR_OCCURRED, e);
        synchronized (listeners) {
            final Iterator it = listeners.iterator();
            while (it.hasNext()) {
                final ConnectionEventListener listener = (ConnectionEventListener) it.next();
                listener.connectionErrorOccurred(event);
            }
        }
    }

    protected void fireLocalTransactionCommitted() {
        final ConnectionEvent event = new ConnectionEvent(this,
                ConnectionEvent.LOCAL_TRANSACTION_COMMITTED);
        synchronized (listeners) {
            final Iterator it = listeners.iterator();
            while (it.hasNext()) {
                final ConnectionEventListener listener = (ConnectionEventListener) it.next();
                listener.localTransactionCommitted(event);
            }
        }
    }

    protected void fireLocalTransactionRolledback() {
        final ConnectionEvent event = new ConnectionEvent(this,
                ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK);
        synchronized (listeners) {
            final Iterator it = listeners.iterator();
            while (it.hasNext()) {
                final ConnectionEventListener listener = (ConnectionEventListener) it.next();
                listener.localTransactionRolledback(event);
            }
        }
    }

    protected void fireLocalTransactionStarted() {
        final ConnectionEvent event = new ConnectionEvent(this,
                ConnectionEvent.LOCAL_TRANSACTION_STARTED);
        synchronized (listeners) {
            final Iterator it = listeners.iterator();
            while (it.hasNext()) {
                final ConnectionEventListener listener = (ConnectionEventListener) it.next();
                listener.localTransactionStarted(event);
            }
        }
    }

    protected void assertPhysicalConnectionOpened() throws ResourceException {
        if (pc == null) {
            throw new SResourceException("");
        }
    }

    protected void assertPhysicalConnectionClosed() throws ResourceException {
        if (pc != null) {
            throw new SResourceException("");
        }
    }

    protected void assertConnectionHandleOpened() throws ResourceException {
        if (activeCh == null) {
            throw new SResourceException("");
        }
    }

    protected void assertConnectionHandleClosed() throws ResourceException {
        if (activeCh != null) {
            throw new SResourceException("");
        }
    }
}
