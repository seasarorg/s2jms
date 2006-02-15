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

import java.lang.reflect.Method;

import javax.jms.Message;
import javax.transaction.TransactionManager;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.JmsContainer;
import org.seasar.jms.container.MessageBinder;
import org.seasar.jms.container.MessageBinderFactory;
import org.seasar.jms.container.annotation.MessageHandler;
import org.seasar.jms.container.exception.MessageHandlerNotFoundRuntimeException;
import org.seasar.jms.container.exception.NotSupportedMessageRuntimeException;

/**
 * @author y-komori
 * 
 */
@Component(instance = InstanceType.SINGLETON)
public class JmsContainerImpl implements JmsContainer {
    private static final String DEFAULT_MESSAGE_HANDLER_NAME = "onMessage";

    private MessageBinderFactory messageBinderFactory = new MessageBinderFactoryImpl();
    private TransactionManager transactionManager;
    private static Logger logger = Logger.getLogger(JmsContainerImpl.class);

    private Object messageHandler;
    private Method messageHandlerMethod;

    public void onMessage(Message message) {
        logger.debug("[S2JMS-Container] onMessage called.");

        bindMessage(message);
        invokeMessageaHandler(messageHandlerMethod.getName());
    }

    @InitMethod
    public void initialize() {
        messageHandlerMethod = findMessageHandler(messageHandler.getClass());
    }

    private void bindMessage(Message message) {
        MessageBinder binder = messageBinderFactory.getMessageBinder(message);
        if (binder != null) {
            binder.bindMessage(messageHandler, message);
        } else {
            throw new NotSupportedMessageRuntimeException("EJMS2002", message);
        }
    }

    private void invokeMessageaHandler(String methodName) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(messageHandler.getClass());
        if (beanDesc != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("[S2JMS-Container] メッセージハンドラを呼び出します. - "
                        + messageHandler.getClass().getName() + "#" + methodName);
            }

            try {
                beanDesc.invoke(messageHandler, methodName, null);
            } catch (Exception ex) {
                logger.error("[S2JMS-Container] メッセージハンドラ内で例外が発生しました.", ex);
                rollBack();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("[S2JMS-Container] メッセージハンドラの呼び出しが終了しました. - "
                        + messageHandler.getClass().getName() + "#" + methodName);
            }
        }
    }

    private Method findMessageHandler(Class clazz) {
        Method messageHandlerMethod = null;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            MessageHandler annotation = method.getAnnotation(MessageHandler.class);
            if (annotation != null) {
                messageHandlerMethod = method;
                break;
            }
        }

        if (messageHandlerMethod == null) {
            try {
                messageHandlerMethod = clazz.getDeclaredMethod(DEFAULT_MESSAGE_HANDLER_NAME,
                        (Class[]) null);
            } catch (NoSuchMethodException e) {
                throw new MessageHandlerNotFoundRuntimeException(clazz.getName());
            }
        }

        return messageHandlerMethod;
    }

    private void rollBack() {
        try {
            if ((transactionManager != null) && (transactionManager.getTransaction() != null)) {
                logger.info("[S2JMS-Container] トランザクションをロールバックします.");
                transactionManager.setRollbackOnly();
            }
        } catch (Exception ex) {
            logger.error("[S2JMS-Container] トランザクションのロールバック中に例外が発生しました.", ex);
        }
    }

    @Binding(bindingType = BindingType.MUST)
    public void setMessageHandler(Object messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setMessageBinderFactory(MessageBinderFactory messageBinderFactory) {
        this.messageBinderFactory = messageBinderFactory;
    }
}
