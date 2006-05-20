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
package org.seasar.jms.core;

import java.io.Serializable;
import java.util.Map;

import javax.jms.Message;

import org.seasar.jms.core.message.MessageHandler;

/**
 * JMSメッセージを受信するコンポーネントのインタフェースです。
 * <p>
 * 受信したメッセージを容易に処理するために次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #receiveBytes}</li>
 * <li>{@link #receiveMap}</li>
 * <li>{@link #receiveObject}</li>
 * <li>{@link #receiveText}</li>
 * </ul>
 * これらのメソッドは受信したJMS標準メッセージ型のペイロードを戻り値として返します。
 * </p>
 * <p>
 * 受信したJMSメッセージを独自に処理するには次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #receive(MessageHandler)}</li>
 * </ul>
 * 引数で指定する{@link org.seasar.jms.core.message.MessageHandler}により自由にJMSメッセージを処理することができます。
 * このメソッドは{@link org.seasar.jms.core.message.MessageHandler#handleMessage}の戻り値をそのまま返します。
 * </p>
 * <p>
 * 受信したJMSメッセージをそのまま取得するには次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #receive()}</li>
 * </ul>
 * </p>
 * <p>
 * いずれの場合もタイムアウトとした場合は{@code null}を返します。
 * </p>
 * 
 * @author koichik
 */
public interface MessageReceiver {
    /**
     * JMSの{@link javax.jms.BytesMessage}を受信してペイロード(メッセージボディ)をバイト配列として返します。
     * 
     * @return 受信した{@link javax.jms.BytesMessage}のペイロードです。タイムアウトとした場合は{@code null}を返します。
     * @throws ClassCastException
     *             受信したJMSメッセージが{@link javax.jms.BytesMessage}ではなかった場合にスローされます。
     */
    byte[] receiveBytes();

    /**
     * JMSの{@link javax.jms.TextMessage}を受信してペイロード(メッセージボディ)を文字列として返します。
     * 
     * @return 受信した{@link javax.jms.TextMessage}のペイロードです。タイムアウトとした場合は{@code null}を返します。
     * @throws ClassCastException
     *             受信したJMSメッセージが{@link javax.jms.TextMessage}ではなかった場合にスローされます。
     */
    String receiveText();

    /**
     * JMSの{@link javax.jms.ObjectMessage}を受信してペイロード(メッセージボディ)を{@link java.io.Serializable}オブジェクトとして返します。
     * 
     * @return 受信した{@link javax.jms.ObjectMessage}のペイロードです。タイムアウトとした場合は{@code null}を返します。
     * @throws ClassCastException
     *             受信したJMSメッセージが{@link javax.jms.ObjectMessage}ではなかった場合にスローされます。
     */
    Serializable receiveObject();

    /**
     * JMSの{@link javax.jms.MapMessage}を受信してペイロード(メッセージボディ)を{@link java.util.Map}として返します。
     * 
     * @return 受信した{@link javax.jms.MapMessage}のペイロードです。タイムアウトとした場合は{@code null}を返します。
     * @throws ClassCastException
     *             受信したJMSメッセージが{@link javax.jms.MapMessage}ではなかった場合にスローされます。
     */
    Map<String, Object> receiveMap();

    /**
     * JMSメッセージを受信して{@link MessageHandler}が処理した結果を返します。
     * 
     * @param messageHandler
     *            受信したメッセージを処理する{@link MessageHandler}
     * @return 受信したJMSメッセージを{@link MessageHandler}が処理した結果です。タイムアウトとした場合は{@code null}を返します。
     */
    <MSGTYPE extends Message, T> T receive(MessageHandler<MSGTYPE, T> messageHandler);

    /**
     * JMSメッセージを受信してそのまま返します。
     * 
     * @return 受信したJMSメッセージです。タイムアウトとした場合は{@code null}を返します。
     */
    Message receive();
}
