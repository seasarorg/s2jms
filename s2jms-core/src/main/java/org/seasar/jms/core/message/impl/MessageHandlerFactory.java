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

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jms.Message;

import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.framework.util.tiger.Tuple3;
import org.seasar.jms.core.message.MessageHandler;

import static org.seasar.framework.util.tiger.CollectionsUtil.*;
import static org.seasar.framework.util.tiger.GenericUtil.*;

/**
 * JMSメッセージ型に対応する{@link org.seasar.jms.core.message.MessageHandler}実装クラスの
 * インスタンスを提供するクラスです。
 * 
 * @author y-komori
 * 
 */
public class MessageHandlerFactory {

    // static fields
    /** {@link MessageHandler}のMSGTYPE型変数 */
    protected static final Type MSGTYPE = MessageHandler.class.getTypeParameters()[0];

    /** {@link MessageHandler}のPAYLOADTYPE型変数 */
    protected static final Type PAYLOADTYPE = MessageHandler.class.getTypeParameters()[1];

    /** {@link MessageHandler}とそのメッセージタイプ，ペイロードタイプの組からなる{@link List} */
    protected static final LinkedList<Tuple3<Class<? extends MessageHandler<? extends Message, ?>>, Class<?>, Class<?>>> handlers = newLinkedList();
    static {
        addMessageHandler(ObjectMessageHandler.class);
        addMessageHandler(BytesMessageHandler.class);
        addMessageHandler(MapMessageHandler.class);
        addMessageHandler(TextMessageHandler.class);
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
    @SuppressWarnings("unchecked")
    public static void addMessageHandler(final MessageHandler<? extends Message, ?> handler) {
        addMessageHandler((Class<? extends MessageHandler<? extends Message, ?>>) handler
                .getClass());
    }

    /**
     * {@link org.seasar.jms.core.message.MessageHandler}を追加します。
     * <p>
     * 追加される{@link org.seasar.jms.core.message.MessageHandler}はリストの先頭に加えられます。
     * </p>
     * 
     * @param handler
     *            追加されるメッセージハンドラのクラス
     */
    public static void addMessageHandler(
            final Class<? extends MessageHandler<? extends Message, ?>> handlerClass) {
        final Map<TypeVariable<?>, Type> typeVariableMap = getTypeVariableMap(handlerClass);
        final Class<?> messageType = getActualClass(MSGTYPE, typeVariableMap);
        final Class<?> payloadType = getActualClass(PAYLOADTYPE, typeVariableMap);
        handlers
                .addFirst(new Tuple3<Class<? extends MessageHandler<? extends Message, ?>>, Class<?>, Class<?>>(
                        handlerClass, messageType, payloadType));
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
    public static MessageHandler<? extends Message, ?> getMessageHandlerFromMessageType(
            final Class<? extends Message> messageClass) {
        for (final Tuple3<Class<? extends MessageHandler<? extends Message, ?>>, Class<?>, Class<?>> tuple : handlers) {
            if (tuple.getValue2().isAssignableFrom(messageClass)) {
                return ReflectionUtil.newInstance(tuple.getValue1());
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
    public static MessageHandler<? extends Message, ?> getMessageHandlerFromPayloadType(
            final Class<?> payloadType) {
        for (final Tuple3<Class<? extends MessageHandler<? extends Message, ?>>, Class<?>, Class<?>> tuple : handlers) {
            if (tuple.getValue3().isAssignableFrom(payloadType)) {
                return ReflectionUtil.newInstance(tuple.getValue1());
            }
        }
        return null;
    }

}
