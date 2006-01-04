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

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;

import org.seasar.jms.connector.sh.QueueSessionHandleImpl;

/**
 * @author koichik
 */
public class QueueConnectionHandleImpl extends ConnectionHandleImpl implements QueueConnection {
    public QueueConnectionHandleImpl() {
    }

    protected QueueConnection getPhysicalQueueConnection() {
        return (QueueConnection) pc;
    }

    public ConnectionConsumer createConnectionConsumer(final Queue queue,
            final String messageSelector, final ServerSessionPool sessionPool, final int maxMessages)
            throws JMSException {
        return getPhysicalQueueConnection().createConnectionConsumer(queue, messageSelector,
                sessionPool, maxMessages);
    }

    public QueueSession createQueueSession(final boolean transacted, int acknowledgeMode)
            throws JMSException {
        assertConnectionOpend();
        assertSessionClosed();
        sh = new QueueSessionHandleImpl();
        sh.associateSession(this, mc.getPhysicalSession(transacted, acknowledgeMode));
        return (QueueSession) sh;
    }
}
