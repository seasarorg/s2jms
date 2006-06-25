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

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SIllegalArgumentException;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.JMSContainer;
import org.seasar.jms.container.JMSRequest;
import org.seasar.jms.container.exception.NotSupportedMessageException;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.MessageHandlerFactory;

/**
 * @author y-komori
 * 
 */
@Component(instance = InstanceType.SINGLETON)
public class JMSContainerImpl implements JMSContainer {

    protected static Logger logger = Logger.getLogger(JMSContainerImpl.class);

    protected TransactionManager transactionManager;

    protected List<String> messageListenerNames = new ArrayList<String>();

    protected Map<Class<?>, MessageListenerSupport> listenerSupportMap = new HashMap<Class<?>, MessageListenerSupport>();

    protected S2Container container;

    public void onMessage(final Message message) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("[S2JMS-Container] onMessage が呼び出されました.");
            }

            setRequest(message);

            for (final String targetName : messageListenerNames) {
                final Object target = container.getComponent(targetName);
                invokeMessageHandler(target);
            }
        } catch (final Exception ex) {
            logger.error("[S2JMS-Container] onMessage 処理中に例外が発生しました.", ex);
            rollBack();
        } finally {
            getExternalContext().setRequest(null);
        }
    }

    public void addMessageListener(final String messageListenerName) {
        if (messageListenerName == null) {
            throw new SIllegalArgumentException("EJMS2005", null);
        }

        ComponentDef cd = container.getComponentDef(messageListenerName);
        final Class<?> clazz = cd.getComponentClass();
        listenerSupportMap.put(clazz, new MessageListenerSupport(clazz));
        messageListenerNames.add(messageListenerName);
    }

    protected void setRequest(final Message message) {
        JMSRequest request = new JMSRequestImpl(message);
        container.getRoot().getExternalContext().setRequest(request);
    }

    protected ExternalContext getExternalContext() {
        ExternalContext externalContext = container.getRoot()
                .getExternalContext();
        if (externalContext == null) {
            throw new EmptyRuntimeException("externalContext");
        }
        return externalContext;
    }

    protected MessageHandler<?, ?> getMessageHandler(final Message message) {
        final MessageHandler<?, ?> messageHandler = MessageHandlerFactory
                .getMessageHandler(message.getClass());
        if (messageHandler == null) {
            throw new NotSupportedMessageException(message);
        }
        return messageHandler;
    }

    protected void invokeMessageHandler(final Object invokeTarget) {
        final MessageListenerSupport support = listenerSupportMap
                .get(invokeTarget.getClass());
        String signature = null;

        if (logger.isDebugEnabled()) {
            signature = invokeTarget.getClass().getName() + "#"
                    + support.getListenerMethodName() + "@"
                    + Integer.toHexString(invokeTarget.hashCode());
            logger.debug("[S2JMS-Container] メッセージハンドラを呼び出します. - " + signature);
        }

        support.invoke(invokeTarget);

        if (logger.isDebugEnabled()) {
            logger.debug("[S2JMS-Container] メッセージハンドラの呼び出しが終了しました. - "
                    + signature);
        }
    }

    protected void rollBack() {
        try {
            if ((transactionManager != null)
                    && (transactionManager.getTransaction() != null)) {
                logger.info("[S2JMS-Container] トランザクションをロールバックします.");
                transactionManager.setRollbackOnly();
            }
        } catch (final Exception ex) {
            logger.error("[S2JMS-Container] トランザクションのロールバック中に例外が発生しました.", ex);
        }
    }

    @Binding(bindingType = BindingType.MAY)
    public void setTransactionManager(
            final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setContainer(S2Container container) {
        this.container = container;
    }
}
