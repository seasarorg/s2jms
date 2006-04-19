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
import javax.jms.XAQueueConnection;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionFactory;

import org.seasar.jms.connector.ConnectionHandle;
import org.seasar.jms.connector.ch.QueueConnectionHandleImpl;

/**
 * @author koichik
 */
public class XAQueueManagedConnectionImpl extends XAManagedConnectionImpl {
    public XAQueueManagedConnectionImpl(final ConnectionRequestInfo info,
            final ManagedConnectionFactory mcf, final XAQueueConnection pc)
            throws ResourceException {
        super(info, mcf, pc);
    }

    @Override
    public ConnectionHandle createConnectionHandle() throws ResourceException {
        return new QueueConnectionHandleImpl();
    }

    @Override
    public Session createPhysicalSession(boolean transacted, int acknowlegeMode)
            throws JMSException {
        return ((XAQueueConnection) pc).createXAQueueSession();
    }
}
