/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.jms.core.message;

import javax.jms.Message;

/**
 * 受信したJMSメッセージを処理するコンポーネントのインタフェースです。
 * <p>
 * {@link MessageHandler}は処理対象となるJMSメッセージ型とのそのペイロード型を型引数として持ちます。 JMSメッセージ型とは{@link javax.jms.Message}を拡張したインタフェースまたは実装したクラスで、ペイロード型はJMSメッセージ型の持つペイロードの型です。<br>
 * 受信したJMSメッセージが{@link MessageHandler}の処理対象となるJMSメッセージ型に代入可能でない場合、そのメッセージは{@link #handleMessage}に渡されません。
 * </p>
 * 
 * @param <MSGTYPE>
 *            JMSメッセージの型
 * @param <PAYLOADTYPE>
 *            JMSメッセージのペイロードの型
 * @author koichik
 */
public interface MessageHandler<MSGTYPE extends Message, PAYLOADTYPE> {

    /**
     * 処理対象となるJMSメッセージの型を返します。
     * 
     * @return JMSメッセージの型
     */
    Class<MSGTYPE> getMessageType();

    /**
     * 処理対象となるJMSメッセージのペイロード型を返します。
     * 
     * @return JMSメッセージのペイロード型
     */
    Class<? super PAYLOADTYPE> getPayloadType();

    /**
     * JMSメッセージを処理してペイロードを返します。
     * 
     * @param message
     *            受信したJMSメッセージ
     * @return JMSメッセージのペイロード
     */
    PAYLOADTYPE handleMessage(MSGTYPE message);

}
