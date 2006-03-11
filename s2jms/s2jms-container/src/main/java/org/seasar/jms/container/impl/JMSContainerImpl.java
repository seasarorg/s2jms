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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.transaction.TransactionManager;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.exception.SIllegalArgumentException;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.JMSContainer;
import org.seasar.jms.container.exception.NotSupportedMessageException;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.MessageHandlerFactory;

/**
 * @author y-komori
 * 
 */
@Component(instance = InstanceType.PROTOTYPE)
public class JMSContainerImpl implements JMSContainer {
    protected static Logger logger = Logger.getLogger(JMSContainerImpl.class);

    protected TransactionManager transactionManager;
    protected List<Object> messageListeners = new ArrayList<Object>();
    protected Map<Class<?>, MessageListenerSupport> listenerSupportMap = new HashMap<Class<?>, MessageListenerSupport>();

    public void onMessage(final Message message) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("[S2JMS-Container] onMessage が呼び出されました.");
            }

            final MessageHandler<?, ?> messageHandler = getMessageHandler(message);

            for (final Object target : messageListeners) {
                bindMessage(target, messageHandler, message);
                invokeMessageHandler(target);
            }
        } catch (final Exception ex) {
            logger.error("[S2JMS-Container] onMessage 処理中に例外が発生しました.", ex);
            rollBack();
        }
    }

    public void addMessageListener(final Object messageListener) {
        if (messageListener == null) {
            throw new SIllegalArgumentException("EJMS2005", null);
        }
        messageListeners.add(messageListener);
        final Class<?> clazz = messageListener.getClass();
        listenerSupportMap.put(clazz, new MessageListenerSupport(clazz));
    }

    protected MessageHandler<?, ?> getMessageHandler(final Message message) {
        final MessageHandler<?, ?> messageHandler = MessageHandlerFactory.getMessageHandler(message
                .getClass());
        if (messageHandler == null) {
            throw new NotSupportedMessageException(message);
        }
        return messageHandler;
    }

    protected <MSGTYPE extends Message, PAYLOADTYPE> void bindMessage(final Object bindTarget,
            final MessageHandler<MSGTYPE, PAYLOADTYPE> handler, final Message message) {
        final Object payload = handler.handleMessage(handler.getMessageType().cast(message));
        final MessageListenerSupport support = listenerSupportMap.get(bindTarget.getClass());
        support.bind(bindTarget, message, payload);
    }

    protected void invokeMessageHandler(final Object invokeTarget) {
        final MessageListenerSupport support = listenerSupportMap.get(invokeTarget.getClass());

        if (logger.isDebugEnabled()) {
            final String className = invokeTarget.getClass().getName();
            final String methodName = support.getListenerMethodName();
            logger.debug("[S2JMS-Container] メッセージハンドラを呼び出します. - " + className + "#" + methodName);
        }

        support.invoke(invokeTarget);

        if (logger.isDebugEnabled()) {
            final String className = invokeTarget.getClass().getName();
            final String methodName = support.getListenerMethodName();
            logger.debug("[S2JMS-Container] メッセージハンドラの呼び出しが終了しました. - " + className + "#"
                    + methodName);
        }
    }

    protected void rollBack() {
        try {
            if ((transactionManager != null) && (transactionManager.getTransaction() != null)) {
                logger.info("[S2JMS-Container] トランザクションをロールバックします.");
                transactionManager.setRollbackOnly();
            }
        } catch (final Exception ex) {
            logger.error("[S2JMS-Container] トランザクションのロールバック中に例外が発生しました.", ex);
        }
    }

    @Binding(bindingType = BindingType.MAY)
    public void setTransactionManager(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
