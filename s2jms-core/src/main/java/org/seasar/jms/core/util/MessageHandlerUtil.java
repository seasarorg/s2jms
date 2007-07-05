/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.jms.core.util;

import javax.jms.Message;

import org.seasar.jms.core.message.MessageHandler;

/**
 * {@link MessageHandler}を扱うためのユーティリティ。
 * 
 * @author koichik
 */
public class MessageHandlerUtil extends JMSHeaderSupport {

    /**
     * JMSメッセージのペイロードを返します。
     * 
     * @param <MSGTYPE>
     *            JMSメッセージの型
     * @param <PAYLOADTYPE>
     *            JMAメッセージのペイロードの型
     * @param handler
     *            メッセージハンドラ
     * @param message
     *            JMSメッセージ
     * @return JMSメッセージのペイロード
     */
    public static <MSGTYPE extends Message, PAYLOADTYPE> PAYLOADTYPE getPayload(
            final MessageHandler<MSGTYPE, PAYLOADTYPE> handler, final Message message) {
        return handler.handleMessage(handler.getMessageType().cast(message));
    }

}
