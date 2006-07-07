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
package org.seasar.jms.container.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.seasar.framework.container.external.AbstractUnmodifiableExternalContextMap;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.jms.container.exception.NotSupportedMessageException;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.MessageHandlerFactory;
import org.seasar.jms.core.util.IterableAdapter;

/**
 * @author shot
 * @author higa
 */
public class JMSRequestParameterMap extends
        AbstractUnmodifiableExternalContextMap {

    protected static final String PAYLOAD_NAME = "payload";

    protected final Message message;

    protected final MessageHandler messageHandler;

    protected final Set<String> names;

    public JMSRequestParameterMap(final Message message) {
        this.message = message;
        this.messageHandler = MessageHandlerFactory.getMessageHandler(message
                .getClass());
        if (messageHandler == null) {
            throw new NotSupportedMessageException(message);
        }

        final Set<String> tempNames = CollectionsUtil.newHashSet();
        tempNames.add(PAYLOAD_NAME);
        if (MapMessage.class.isInstance(message)) {
            try {
                final MapMessage mapMessage = MapMessage.class.cast(message);
                for (final String name : new IterableAdapter(mapMessage
                        .getMapNames())) {
                    tempNames.add(name);
                }
            } catch (final JMSException e) {
                throw new SJMSRuntimeException("EJMS0000", e);
            }
        }
        names = Collections.unmodifiableSet(tempNames);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object getAttribute(String key) {
        if (PAYLOAD_NAME.equals(key)) {
            return messageHandler.handleMessage(message);
        }
        if (MapMessage.class.isInstance(message)) {
            try {
                return MapMessage.class.cast(message).getObject(key);
            } catch (final JMSException e) {
                throw new SJMSRuntimeException("EJMS0000", e);
            }
        }
        return null;
    }

    @Override
    protected Iterator getAttributeNames() {
        return names.iterator();
    }

}
