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

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnectionFactory;
import javax.transaction.xa.XAResource;

import org.seasar.jca.exception.SNotSupportedException;
import org.seasar.jca.exception.SResourceException;
import org.seasar.jms.connector.ConnectionHandle;
import org.seasar.jms.connector.ch.ConnectionHandleImpl;

/**
 * @author koichik
 */
public class ManagedConnectionImpl extends AbstractManagedConnection implements LocalTransaction {
    protected boolean managedTx;

    public ManagedConnectionImpl(final ConnectionRequestInfo info,
            final ManagedConnectionFactory mcf, final Connection pc) throws ResourceException {
        super(info, mcf, pc);
    }

    @Override
    public ConnectionHandle createConnectionHandle() throws ResourceException {
        return new ConnectionHandleImpl();
    }

    @Override
    protected Session createPhysicalSession(boolean transacted, int acknowledgeMode)
            throws JMSException {
        return pc.createSession(transacted, acknowledgeMode);
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        try {
            ps = createPhysicalSession(true, Session.SESSION_TRANSACTED);
            managedTx = true;
            return this;
        } catch (JMSException e) {
            throw new SResourceException("EJMS0000", e);
        }
    }

    public XAResource getXAResource() throws ResourceException {
        throw new SNotSupportedException("");
    }

    public boolean isManagedTx() {
        return managedTx;
    }

    public void begin() throws ResourceException {
    }

    public void commit() throws ResourceException {
        try {
            ps.commit();
        } catch (JMSException e) {
            throw new SResourceException("EJMS0000", e);
        }
    }

    public void rollback() throws ResourceException {
        try {
            ps.rollback();
        } catch (final JMSException e) {
            throw new SResourceException("EJMS0000", e);
        }
    }
}
