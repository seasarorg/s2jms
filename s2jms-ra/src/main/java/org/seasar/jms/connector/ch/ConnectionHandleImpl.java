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
package org.seasar.jms.connector.ch;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

import org.seasar.jms.connector.ConnectionHandle;
import org.seasar.jms.connector.JMSManagedConnection;
import org.seasar.jms.connector.SessionHandle;
import org.seasar.jms.connector.sh.SessionHandleImpl;
import org.seasar.jms.core.exception.SJMSException;

/**
 * @author koichik
 */
public class ConnectionHandleImpl implements ConnectionHandle, Connection {
    protected JMSManagedConnection mc;
    protected Connection pc;
    protected SessionHandle sh;
    protected ExceptionListener listener;
    protected boolean active;
    protected int acknowledgeMode;

    public ConnectionHandleImpl() {
    }

    public JMSManagedConnection getManagedConnection() {
        return mc;
    }

    public void associateConnection(final JMSManagedConnection mc, final Connection pc)
            throws JMSException {
        this.mc = mc;
        this.pc = pc;
        this.active = true;
        if (sh != null) {
            final Session session = (Session) sh;
            sh.associateSession(this, mc.getPhysicalSession(session.getTransacted(), session
                    .getAcknowledgeMode()));
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public void cleanup() {
        mc = null;
        pc = null;
        sh = null;
        listener = null;
        active = false;
        acknowledgeMode = -1;
    }

    public void onException(final JMSException e) {
        if (listener != null) {
            listener.onException(e);
        }
    }

    public boolean isManagedTx() {
        return mc.isManagedTx();
    }

    public int getAcknowledgeMode() throws JMSException {
        assertConnectionOpend();
        assertSessionOpened();
        return acknowledgeMode;
    }

    public void sessionClosed(final SessionHandle sh) throws JMSException {
        assertActiveSessionHandle(sh);
        sh.cleanup();
        this.sh = null;
        mc.sessionHandleClosed(this);
    }

    public Session createSession(final boolean transacted, final int acknowledgeMode)
            throws JMSException {
        assertConnectionOpend();
        assertSessionClosed();
        sh = new SessionHandleImpl();
        sh.associateSession(this, mc.getPhysicalSession(transacted, acknowledgeMode));
        this.acknowledgeMode = acknowledgeMode;
        return (Session) sh;
    }

    public void close() throws JMSException {
        if (active) {
            mc.connectionHandleClosed(this);
        }
    }

    protected void assertConnectionOpend() throws JMSException {
        if (!active) {
            throw new JMSException("");
        }
    }

    protected void assertActiveSessionHandle(SessionHandle sh) throws JMSException {
        if (this.sh != sh) {
            throw new SJMSException("");
        }
    }

    protected void assertSessionOpened() throws JMSException {
        if (sh == null) {
            throw new JMSException("");
        }
    }

    protected void assertSessionClosed() throws JMSException {
        if (sh != null) {
            throw new JMSException("");
        }
    }

    public ConnectionConsumer createConnectionConsumer(final Destination destination,
            final String messageSelector, final ServerSessionPool sessionPool, final int maxMessages)
            throws JMSException {
        assertConnectionOpend();
        return pc.createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages);
    }

    public ConnectionConsumer createDurableConnectionConsumer(final Topic topic,
            final String subscriptionName, final String messageSelector,
            final ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        assertConnectionOpend();
        return pc.createDurableConnectionConsumer(topic, subscriptionName, messageSelector,
                sessionPool, maxMessages);
    }

    public String getClientID() throws JMSException {
        assertConnectionOpend();
        return pc.getClientID();
    }

    public ExceptionListener getExceptionListener() throws JMSException {
        assertConnectionOpend();
        return listener;
    }

    public ConnectionMetaData getMetaData() throws JMSException {
        assertConnectionOpend();
        return pc.getMetaData();
    }

    public void setClientID(final String clientId) throws JMSException {
        assertConnectionOpend();
        pc.setClientID(clientId);
    }

    public void setExceptionListener(final ExceptionListener listener) throws JMSException {
        assertConnectionOpend();
        this.listener = listener;
    }

    public void start() throws JMSException {
        assertConnectionOpend();
        pc.start();
    }

    public void stop() throws JMSException {
        assertConnectionOpend();
        pc.stop();
    }
}
