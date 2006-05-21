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
package org.seasar.jms.core.message.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Message;

import org.seasar.jms.core.message.MessageHandler;

/**
 * JMSメッセージ型と、対応する{@link org.seasar.jms.core.message.MessageHandler}実装クラスの
 * マッピングを保持するクラスです。
 * 
 * @author y-komori
 * 
 */
public class MessageHandlerFactory {
    protected static Map<Class<? extends Message>, MessageHandler<?, ?>> handlerMap = new HashMap<Class<? extends Message>, MessageHandler<?, ?>>();
    protected static List<MessageHandler<?, ?>> handlers = new ArrayList<MessageHandler<?, ?>>();
    static {
        handlers.add(new TextMessageHandler());
        handlers.add(new MapMessageHandler());
        handlers.add(new BytesMessageHandler());
        handlers.add(new ObjectMessageHandler());
    }

    /**
     * 利用者がインスタンスを構築しないようにするための{@code private}コンストラクタです。
     * 
     */
    private MessageHandlerFactory() {
    }

    /**
     * JMSメッセージ型に対応する{@link org.seasar.jms.core.message.MessageHandler}実装クラスを返します。
     * 
     * @param messageClass
     *            JMSメッセージ型
     * @return JMSメッセージ型に対応する{@link org.seasar.jms.core.message.MessageHandler}実装クラス。
     *         対応するクラスがない場合は{@code null}
     */
    public static MessageHandler<?, ?> getMessageHandler(final Class<? extends Message> messageClass) {
        for (final MessageHandler<?, ?> messageHandler : handlers) {
            if (messageHandler.getMessageType().isAssignableFrom(messageClass)) {
                return messageHandler;
            }
        }
        return null;
    }
}
