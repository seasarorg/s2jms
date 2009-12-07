/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.jms.core.message.impl;

import java.util.LinkedList;
import java.util.Map;

import javax.jms.Message;

import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.jms.core.message.MessageHandler;

/**
 * JMSメッセージ型に対応する{@link org.seasar.jms.core.message.MessageHandler}実装クラスの
 * インスタンスを提供するクラスです。
 * 
 * @author y-komori
 * 
 */
public class MessageHandlerFactory {

    // static fields
    /** {@link MessageHandler}の{@link Map} */
    protected static LinkedList<MessageHandler<? extends Message, ?>> handlers = CollectionsUtil
            .newLinkedList();
    static {
        handlers.add(new TextMessageHandler());
        handlers.add(new MapMessageHandler());
        handlers.add(new BytesMessageHandler());
        handlers.add(new ObjectMessageHandler());
    }

    /**
     * {@link org.seasar.jms.core.message.MessageHandler}を追加します。
     * <p>
     * 追加される{@link org.seasar.jms.core.message.MessageHandler}はリストの先頭に加えられます。
     * </p>
     * 
     * @param handler
     *            追加されるメッセージハンドラ
     */
    public static void addMessageHandler(final MessageHandler<?, ?> handler) {
        handlers.addFirst(handler);
    }

    /**
     * 利用者がインスタンスを構築しないようにするための{@code private}コンストラクタです。
     * 
     */
    private MessageHandlerFactory() {
    }

    /**
     * JMSメッセージ型に対応する{@link org.seasar.jms.core.message.MessageHandler}
     * 実装クラスを返します。
     * 
     * @param messageClass
     *            JMSメッセージ型
     * @return JMSメッセージ型に対応する{@link org.seasar.jms.core.message.MessageHandler}
     *         実装クラス。 対応するクラスがない場合は{@code null}
     */
    @SuppressWarnings("unchecked")
    public static MessageHandler<? extends Message, ?> getMessageHandlerFromMessageType(
            final Class<? extends Message> messageClass) {
        for (final MessageHandler<? extends Message, ?> messageHandler : handlers) {
            if (messageHandler.getMessageType().isAssignableFrom(messageClass)) {
                return ReflectionUtil.newInstance(messageHandler.getClass());
            }
        }
        return null;
    }

    /**
     * JMSメッセージのペイロード型に対応する{@link org.seasar.jms.core.message.MessageHandler}
     * 実装クラスを返します。
     * 
     * @param payloadType
     *            JMSメッセージのペイロード型
     * @return JMSメッセージ型に対応する{@link org.seasar.jms.core.message.MessageHandler}
     *         実装クラス。 対応するクラスがない場合は{@code null}
     */
    @SuppressWarnings("unchecked")
    public static MessageHandler<? extends Message, ?> getMessageHandlerFromPayloadType(
            final Class<?> payloadType) {
        for (final MessageHandler<? extends Message, ?> messageHandler : handlers) {
            if (messageHandler.getPayloadType().isAssignableFrom(payloadType)) {
                return ReflectionUtil.newInstance(messageHandler.getClass());
            }
        }
        return null;
    }

}
