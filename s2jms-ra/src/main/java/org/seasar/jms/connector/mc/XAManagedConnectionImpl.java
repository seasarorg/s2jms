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

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.XAConnection;
import javax.jms.XASession;
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
public class XAManagedConnectionImpl extends AbstractManagedConnection {
    public XAManagedConnectionImpl(final ConnectionRequestInfo info,
            final ManagedConnectionFactory mcf, final XAConnection pc) throws ResourceException {
        super(info, mcf, pc);
    }

    @Override
    public ConnectionHandle createConnectionHandle() throws ResourceException {
        return new ConnectionHandleImpl();
    }

    @Override
    public Session createPhysicalSession(final boolean transacted, final int acknowlegeMode)
            throws JMSException {
        return ((XAConnection) pc).createXASession();
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        assertConnectionHandleOpened();
        throw new SNotSupportedException("");
    }

    public XAResource getXAResource() throws ResourceException {
        assertPhysicalConnectionOpened();
        if (xa == null) {
            if (ps == null) {
                try {
                    ps = createPhysicalSession(true, Session.SESSION_TRANSACTED);
                } catch (final JMSException e) {
                    throw new SResourceException("EJMS0000", e);
                }
            }
            xa = ((XASession) ps).getXAResource();
        }
        return xa;
    }

    public boolean isManagedTx() {
        return xa != null;
    }
}
