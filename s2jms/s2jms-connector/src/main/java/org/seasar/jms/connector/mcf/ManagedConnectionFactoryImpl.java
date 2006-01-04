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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnection;

import org.seasar.jms.connector.cf.ConnectionFactoryImpl;
import org.seasar.jms.connector.mc.ManagedConnectionImpl;
import org.seasar.jms.connector.support.ConnectionRequestInfoImpl;

/**
 * @author koichik
 */
public class ManagedConnectionFactoryImpl extends AbstractManagedConnectionFactory {
    private static final long serialVersionUID = 1L;

    public Object createConnectionFactory(final ConnectionManager cm) throws ResourceException {
        return new ConnectionFactoryImpl(this, cm);
    }

    @Override
    protected ManagedConnection createManagedConnection(final ConnectionRequestInfoImpl info)
            throws ResourceException, JMSException {
        final ConnectionFactory cf = (ConnectionFactory) getConnectionFactory();
        final Connection con = (info == null) ? cf.createConnection() : cf.createConnection(info
                .getUser(), info.getPassword());
        return new ManagedConnectionImpl(info, this, con);
    }
}
