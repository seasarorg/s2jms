/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.jms.Message;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SRuntimeException;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.jms.container.JMSContainer;
import org.seasar.jms.container.exception.NotSupportedMessageException;
import org.seasar.jms.container.filter.Filter;
import org.seasar.jms.container.filter.FilterChain;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.MessageHandlerFactory;
import org.seasar.jms.core.util.MessageHandlerUtil;

/**
 * S2JMS-Containerの実装クラスです。
 * <p>
 * S2JMS-ContainerはJCAのメッセージエンドポイントとして受信したJMSメッセージを受け取り、
 * 登録されているリスナ・コンポーネントのリスナ・メソッドを呼び出します。リスナ・コンポーネントはそのコンポーネント名を{@link #addMessageListener(String)}メソッドで登録します。
 * S2JMS-Containerはメッセージを受信するたびにS2コンテナからリスナ・コンポーネントを名前でルックアップしますします。
 * </p>
 * <p>
 * S2JMS-Containerにフィルタを設定することにより、リスナ・メソッドを呼び出すまでに任意の処理を組み込むことが可能です。
 * </p>
 * 
 * @author y-komori
 */
@Component
public class JMSContainerImpl implements JMSContainer, Disposable {

    // instance fields
    /** S2コンテナ */
    @Binding(bindingType = BindingType.MUST)
    protected S2Container container;

    /** フィルタの配列 */
    @Binding(bindingType = BindingType.MAY)
    protected Filter[] filters;

    /** インスタンスが初期化済みなら<code>true</code> */
    protected boolean initialized;

    /** メッセージリスナのコンポーネント名の配列 */
    protected List<String> messageListeners = CollectionsUtil.newArrayList();

    /** リスナコンポーネントにJMSメッセージをバインドするコンポーネントの{@link Map} */
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
     * @throws RuntimeException
     *             リスナ・コンポーネントまたはフィルタで例外が発生した場合にスローされます
     */
    public void onMessage(final Message message) {
        initialize();
        try {
            final FilterChain filterChain = new FilterChainImpl();
            filterChain.doFilter(message);
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new SRuntimeException("EJMS0000", null, e);
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
     * メッセージリスナーコンポーネントのリスナーメソッドを呼び出します。
     * 
     * @param message
     *            JMSメッセージ
     * @throws Exception
     *             リスナーメソッドの処理中に例外が発生した場合にスローされます
     */
    protected void invokeMessageListeners(final Message message) throws Exception {
        final MessageHandler<?, ?> messageHandler = MessageHandlerFactory
                .getMessageHandlerFromMessageType(message.getClass());
        if (messageHandler == null) {
            throw new NotSupportedMessageException(message);
        }
        final Object payload = MessageHandlerUtil.getPayload(messageHandler, message);
        for (final String listenerName : messageListeners) {
            final Object listener = container.getRoot().getComponent(listenerName);
            final MessageListenerSupport support = getMessageListenerSupport(listener.getClass());
            support.bind(listener, message, payload);
            support.invoke(listener, message);
        }
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

    /**
     * フィルタチェーンの実装クラスです。
     * 
     * @author koichik
     * 
     */
    public class FilterChainImpl implements FilterChain {

        /** 呼び出すフィルタのインデックス */
        protected int index;

        /**
         * インスタンスを構築します。
         */
        public FilterChainImpl() {
        }

        /**
         * 後続のフィルタを呼び出します。
         * <p>
         * 後続のフィルタがなければ{@link org.seasar.jms.container.impl.JMSContainerImpl}に
         * 制御を戻します．
         * </p>
         * 
         * @param message
         *            受信したJMSメッセージ
         */
        public void doFilter(final Message message) throws Exception {
            if (filters != null && filters.length > index) {
                filters[index++].doFilter(message, this);
            } else {
                invokeMessageListeners(message);
            }
        }

    }

}
