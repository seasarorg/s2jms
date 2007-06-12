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

import org.seasar.jms.core.message.MessageFactory;

/**
 * JMSメッセージを送信するコンポーネントのインタフェースです。
 * <p>
 * 送信するJMSメッセージを容易に作成するために次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #send(byte[])}</li>
 * <li>{@link #send(Map)}</li>
 * <li>{@link #send(Serializable)}</li>
 * <li>{@link #send(String)}</li>
 * </ul>
 * これらのメソッドは対応するJMS標準メッセージ型を作成し、引数をペイロードに設定して送信します。
 * </p>
 * <p>
 * 送信するJMSメッセージを詳細に設定するには次のメソッドを使用することができます。
 * <ul>
 * <li>{@link #send(MessageFactory)}</li>
 * <li>{@link #send()}</li>
 * </ul>
 * 引数またはコンポーネント実装クラスのプロパティに設定する{@link org.seasar.jms.core.message.MessageFactory}により自由にJMSメッセージを作成することができます。
 * </p>
 * 
 * @author koichik
 */
public interface MessageSender {

    /**
     * バイト配列を{@link javax.jms.BytesMessage}のペイロードに設定して送信します。
     * 
     * @param bytes
     *            送信するバイト配列
     */
    void send(byte[] bytes);

    /**
     * バイト配列を{@link javax.jms.BytesMessage}のペイロードに設定して送信します。
     * 
     * @param bytes
     *            送信するバイト配列
     * @param properties
     *            JMSメッセージのプロパティ
     */
    void send(byte[] bytes, Map<String, Object> properties);

    /**
     * 文字列を{@link javax.jms.TextMessage}のペイロードに設定して送信します。
     * 
     * @param text
     *            送信する文字列
     */
    void send(String text);

    /**
     * 文字列を{@link javax.jms.TextMessage}のペイロードに設定して送信します。
     * 
     * @param text
     *            送信する文字列
     * @param properties
     *            JMSメッセージのプロパティ
     */
    void send(String text, Map<String, Object> properties);

    /**
     * {@link java.io.Serializable}オブジェクトを{@link javax.jms.ObjectMessage}のペイロードに設定して送信します。
     * 
     * @param object
     *            送信するオブジェクト
     */
    void send(Serializable object);

    /**
     * {@link java.io.Serializable}オブジェクトを{@link javax.jms.ObjectMessage}のペイロードに設定して送信します。
     * 
     * @param object
     *            送信するオブジェクト
     * @param properties
     *            JMSメッセージのプロパティ
     */
    void send(Serializable object, Map<String, Object> properties);

    /**
     * {@link java.util.Map}を{@link javax.jms.MapMessage}のペイロードに設定して送信します。
     * 
     * @param map
     *            送信するマップ
     */
    void send(Map<String, Object> map);

    /**
     * {@link java.util.Map}を{@link javax.jms.MapMessage}のペイロードに設定して送信します。
     * 
     * @param map
     *            送信するマップ
     * @param properties
     *            JMSメッセージのプロパティ
     */
    void send(Map<String, Object> map, Map<String, Object> properties);

    /**
     * 実装クラスのプロパティに設定された{@link MessageFactory}が作成したJMSメッセージを送信します。
     * 
     */
    void send();

    /**
     * {@link MessageFactory}が作成したJMSメッセージを送信します。
     * 
     * @param <MSGTYPE>
     *            JMSメッセージの型
     * @param messageFactory
     *            メッセージファクトリ
     */
    <MSGTYPE extends Message> void send(MessageFactory<MSGTYPE> messageFactory);
}
