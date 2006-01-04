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
package org.seasar.jms.core.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.core.destination.DestinationFactory;
import org.seasar.jms.core.exception.SJMSRuntimeException;

/**
 * @author koichik
 */
public abstract class AbstractMessageProcessor<RESULT, PARAM> {
    protected ConnectionFactory connectionFactory;
    protected boolean startConnection = true;
    protected boolean transacted = true;
    protected int acknowledgeMode = Session.SESSION_TRANSACTED;
    protected DestinationFactory destinationFactory;

    public AbstractMessageProcessor() {
    }

    public AbstractMessageProcessor(final boolean startConnection) {
        this.startConnection = startConnection;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setConnectionFactory(final ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public boolean isTransacted() {
        return transacted;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setTransacted(final boolean transacted) {
        this.transacted = transacted;
    }

    public int getAcknowledgeMode() {
        return acknowledgeMode;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setAcknowledgeMode(final int acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    public boolean isStartConnection() {
        return startConnection;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setStartConnection(final boolean startConnection) {
        this.startConnection = startConnection;
    }

    public DestinationFactory getDestinationFactory() {
        return destinationFactory;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setDestinationFactory(final DestinationFactory destinationFactory) {
        this.destinationFactory = destinationFactory;
    }

    protected RESULT process(final PARAM opaque) {
        try {
            final Connection con = connectionFactory.createConnection();
            try {
                return processConnection(con, opaque);
            } finally {
                con.close();
            }
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    protected RESULT processConnection(final Connection con, final PARAM opaque)
            throws JMSException {
        if (startConnection) {
            con.start();
        }
        try {
            final Session session = con.createSession(transacted, acknowledgeMode);
            try {
                return processSession(session, opaque);
            } finally {
                session.close();
            }
        } finally {
            if (startConnection) {
                con.stop();
            }
        }
    }

    protected abstract RESULT processSession(Session session, PARAM opaque) throws JMSException;

    protected Destination getDestination(final Session session) {
        return destinationFactory.getDestination(session);

    }
}
