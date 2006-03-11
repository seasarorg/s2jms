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

/**
 * @author y-komori
 * 
 */
@Component(instance = InstanceType.PROTOTYPE)
public class JMSContainerImpl implements JMSContainer {
    protected TransactionManager transactionManager;
    protected static Logger logger = Logger.getLogger(JMSContainerImpl.class);

    protected List<Object> messageHandlers = new ArrayList<Object>();
    protected Map<Class, MessageHandlerSupport> handlerSupportMap = new HashMap<Class, MessageHandlerSupport>();

    public void onMessage(Message message) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("[S2JMS-Container] onMessage が呼び出されました.");
            }

            MessageHandler messageHandler = getMessageHandler(message);

            for (Object target : messageHandlers) {
                bindMessage(target, messageHandler, message);
                invokeMessageHandler(target);
            }
            
        } catch (Exception ex) {
            logger.error("[S2JMS-Container] onMessage 処理中に例外が発生しました.", ex);
            rollBack();
        }
    }

    public void addMessageHandler(final Object messageHandler) {       
        if (messageHandler != null) {
            this.messageHandlers.add(messageHandler);
            Class clazz = messageHandler.getClass();
            this.handlerSupportMap.put(clazz, new MessageHandlerSupport(clazz));
        } else {
            throw new SIllegalArgumentException("EJMS2005", null);
        }
    }

    protected MessageHandler getMessageHandler(Message message) {
        MessageHandler messageHandler = MessageHandlerFactory.getMessageHandler(message.getClass());
        if (messageHandler == null) {
            throw new NotSupportedMessageException(message);
        }
        return messageHandler;
    }

    protected void bindMessage(final Object bindTarget, final MessageHandler handler,
            final Message message) {
        Object payload = handler.handleMessage(message);
        MessageHandlerSupport support = handlerSupportMap.get(bindTarget.getClass());
        support.bind(bindTarget, message, payload);
    }

    protected void invokeMessageHandler(final Object invokeTarget) {
        MessageHandlerSupport support = handlerSupportMap.get(invokeTarget.getClass());

        if (logger.isDebugEnabled()) {
            String className = invokeTarget.getClass().getName();
            String methodName = support.getHandlerName();
            logger.debug("[S2JMS-Container] メッセージハンドラを呼び出します. - " + className + "#" + methodName);
        }

        support.invoke(invokeTarget);

        if (logger.isDebugEnabled()) {
            String className = invokeTarget.getClass().getName();
            String methodName = support.getHandlerName();
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
        } catch (Exception ex) {
            logger.error("[S2JMS-Container] トランザクションのロールバック中に例外が発生しました.", ex);
        }
    }

    @Binding(bindingType=BindingType.MAY)
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
