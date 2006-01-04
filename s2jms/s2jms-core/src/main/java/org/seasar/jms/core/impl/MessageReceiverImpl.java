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

import java.io.Serializable;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.jms.core.MessageReceiver;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.BytesMessageHandler;
import org.seasar.jms.core.message.impl.MapMessageHandler;
import org.seasar.jms.core.message.impl.ObjectMessageHandler;
import org.seasar.jms.core.message.impl.TextMessageHandler;

/**
 * @author koichik
 */
@Component
public class MessageReceiverImpl extends AbstractMessageProcessor<Message, Object> implements
        MessageReceiver {
    protected long timeout = -1;
    protected String messageSelector = null;
    protected boolean noLocal = false;

    public MessageReceiverImpl() {
        super(true);
    }

    public long getTimeout() {
        return timeout;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }

    public String getMessageSelector() {
        return messageSelector;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setMessageSelector(final String messageSelector) {
        this.messageSelector = messageSelector;
    }

    public boolean isNoLocal() {
        return noLocal;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setNoLocal(final boolean noLocal) {
        this.noLocal = noLocal;
    }

    public byte[] receiveBytes() {
        return receive(new BytesMessageHandler());
    }

    public String receiveText() {
        return receive(new TextMessageHandler());
    }

    public Serializable receiveObject() {
        return (Serializable) receive(new ObjectMessageHandler());
    }

    public Map<String, Object> receiveMap() {
        return receive(new MapMessageHandler());
    }

    public <MSGTYPE extends Message, T> T receive(final MessageHandler<MSGTYPE, T> messageHandler) {
        Class<MSGTYPE> clazz = messageHandler.getMessageType();
        return messageHandler.handleMessage(clazz.cast(process(null)));
    }

    @Override
    protected Message processSession(final Session session, final Object opaque)
            throws JMSException {
        final Destination destination = getDestination(session);
        final MessageConsumer consumer = session.createConsumer(destination, messageSelector,
                noLocal);
        if (timeout > 0) {
            return consumer.receive(timeout);
        } else if (timeout == 0) {
            return consumer.receiveNoWait();
        } else {
            return consumer.receive();
        }
    }
}
