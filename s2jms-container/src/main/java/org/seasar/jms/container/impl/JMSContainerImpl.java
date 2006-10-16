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

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.jms.Message;
import javax.transaction.TransactionManager;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.jms.container.JMSContainer;
import org.seasar.jms.container.JMSRequest;
import org.seasar.jms.container.external.JMSRequestImpl;

/**
 * S2JMS-Containerの実装クラスです。
 * <p>
 * S2JMS-ContainerはJCAのメッセージエンドポイントとして受信したJMSメッセージを受け取り、
 * 登録されているメッセージリスナーコンポーネントのリスナーメソッドを呼び出します。 メッセージリスナーコンポーネントはそのコンポーネント名を{@link addMessageListener}メソッドで登録します。
 * S2JMS-Containerはメッセージを受信するたびにS2コンテナからメッセージリスナーコンポーネントを名前でルックアップします。
 * その際に、S2JMS-Containerは受信したメッセージをS2コンテナの外部コンテキストのリクエストオブジェクトとして登録するため、
 * メッセージリスナーコンポーネントのインスタンス属性を <code>request</code> または <code>prototype</code>
 * にすることにより、 JMSメッセージやそのヘッダ・プロパティ・ペイロードをメッセージリスナーコンポーネントにインジェクションすることが可能です。
 * </p>
 * 
 * @author y-komori
 * 
 */
@Component
public class JMSContainerImpl implements JMSContainer, Disposable {

    private static Logger logger = Logger.getLogger(JMSContainerImpl.class);

    @Binding(bindingType = BindingType.MUST)
    protected S2Container container;

    @Binding(bindingType = BindingType.MAY)
    protected TransactionManager transactionManager;

    protected boolean initialized;

    protected List<String> messageListeners = CollectionsUtil.newArrayList();

    protected ConcurrentMap<Class<?>, MessageListenerSupport> listenerSupportMap = CollectionsUtil
            .newConcurrentHashMap();

    /**
     * インスタンスを初期化します。
     */
    @InitMethod
    public void initialize() {
        if (!initialized) {
            DisposableUtil.add(this);
            initialized = true;
        }
    }

    /**
     * インスタンスをクリアします。
     */
    public void dispose() {
        listenerSupportMap.clear();
        initialized = false;
    }

    /**
     * JMSメッセージを受信した際に呼び出されます。
     * 
     * @param message
     *            受信したJMSメッセージ
     */
    public void onMessage(final Message message) {
        initialize();
        if (logger.isDebugEnabled()) {
            logger.log("DJMS-CONTAINER2100", new Object[0]);
        }
        setRequest(message);
        try {
            HotdeployUtil.start();
            try {
                for (final String listenerName : messageListeners) {
                    final Object listener = container.getComponent(listenerName);
                    final MessageListenerSupport support = getMessageListenerSupport(listener
                            .getClass());
                    support.invoke(listener, message);
                }
            } finally {
                HotdeployUtil.stop();
            }
        } catch (final Exception e) {
            if (logger.isDebugEnabled()) {
                logger.log("DJMS-CONTAINER2102", new Object[0], e);
            }
            rollBack();
        } finally {
            getExternalContext().setRequest(null);
            if (logger.isDebugEnabled()) {
                logger.log("DJMS-CONTAINER2101", new Object[0]);
            }
        }
    }

    /**
     * メッセージリスナーコンポーネントを登録します。
     * 
     * @param messageListenerName
     *            メッセージリスナーコンポーネント名
     */
    public void addMessageListener(final String messageListenerName) {
        if (StringUtil.isEmpty(messageListenerName)) {
            throw new EmptyRuntimeException("messageListenerName");
        }
        messageListeners.add(messageListenerName);
    }

    /**
     * JMSメッセージを外部コンテキストのリクエストオブジェクトとして設定します。
     * 
     * @param message
     *            JMSメッセージ
     */
    protected void setRequest(final Message message) {
        final JMSRequest request = new JMSRequestImpl(message);
        container.getRoot().getExternalContext().setRequest(request);
    }

    /**
     * 外部コンテキストを返します。
     * 
     * @return 外部コンテキスト
     */
    protected ExternalContext getExternalContext() {
        final ExternalContext externalContext = container.getRoot().getExternalContext();
        if (externalContext == null) {
            throw new EmptyRuntimeException("externalContext");
        }
        return externalContext;
    }

    /**
     * メッセージリスナーコンポーネントのリスナーメソッドを呼び出します。
     * 
     * @param messageListener
     *            メッセージリスナーコンポーネント
     * @param message
     *            JMSメッセージ
     * @throws Exception
     *             リスナーメソッドの処理中に例外が発生した場合にスローされます
     */
    protected void invokeMessageListener(final Object messageListener, final Message message)
            throws Exception {
        initialize();
        final MessageListenerSupport support = getMessageListenerSupport(messageListener.getClass());
        support.invoke(messageListener, message);
    }

    /**
     * メッセージリスナーのサポートオブジェクトを返します。
     * 
     * @param clazz
     *            メッセージリスナークラス
     * @return メッセージリスナーのサポートオブジェクト
     */
    protected MessageListenerSupport getMessageListenerSupport(final Class<?> clazz) {
        final MessageListenerSupport support = listenerSupportMap.get(clazz);
        if (support != null) {
            return support;
        }
        return createMessageListenerSupport(clazz);
    }

    /**
     * メッセージリスナーのサポートオブジェクトを作成して返します。
     * 
     * @param clazz
     *            メッセージリスナークラス
     * @return メッセージリスナーのサポートオブジェクト
     */
    protected MessageListenerSupport createMessageListenerSupport(final Class<?> clazz) {
        final MessageListenerSupport support = new MessageListenerSupport(clazz);
        return CollectionsUtil.putIfAbsent(listenerSupportMap, clazz, support);
    }

    protected void rollBack() {
        try {
            if ((transactionManager != null) && (transactionManager.getTransaction() != null)) {
                logger.log("IJMS-CONTAINER2105", new Object[0]);
                transactionManager.setRollbackOnly();
            }
        } catch (final Exception e) {
            logger.log("EJMS-CONTAINER2106", new Object[0], e);
        }
    }

}
