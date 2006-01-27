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
package org.seasar.jms.core.session.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

public class SessionFactoryImpl implements SessionFactory {
    protected ConnectionFactory connectionFactory;
    protected boolean transacted = true;
    protected int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;

    public SessionFactoryImpl() {
    }

    @Binding(bindingType = BindingType.MUST)
    public void setConnectionFactory(final ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setTransacted(final boolean transacted) {
        this.transacted = transacted;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setAcknowledgeMode(final int acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    public void createSession(final boolean startConnection, final SessionHandler handler) {
        try {
            final Connection connection = connectionFactory.createConnection();
            try {
                processConnection(startConnection, handler, connection);
            } finally {
                connection.close();
            }
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    protected void processConnection(final boolean startConnection, final SessionHandler handler,
            final Connection connection) throws JMSException {
        if (startConnection) {
            connection.start();
        }
        try {
            final Session session = connection.createSession(transacted, acknowledgeMode);
            try {
                handler.handleSession(session);
            } finally {
                session.close();
            }
        } finally {
            if (startConnection) {
                connection.stop();
            }
        }
    }
}
