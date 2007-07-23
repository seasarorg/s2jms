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
package org.seasar.jms.core;

import java.io.Serializable;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.Message;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.jms.core.exception.SJMSRuntimeException;
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
 * </p>
 * <ul>
 * <li>{@link #send(MessageFactory)}</li>
 * <li>{@link #send()}</li>
 * </ul>
 * <p>
 * 引数またはコンポーネント実装クラスのプロパティに設定する{@link org.seasar.jms.core.message.MessageFactory}により自由にJMSメッセージを作成することができます。
 * </p>
 * <p>
 * 送信したメッセージは{@link #getMessage()}メソッドで取得することができます．
 * 送信したメッセージにJMS実装が設定するJMSヘッダを以下のメソッドで取得することができます．
 * </p>
 * <ul>
 * <li>{@link #getMessageID()}</li>
 * <li>{@link #getTimestamp()}</li>
 * <li>{@link #getExpiration()}</li>
 * </ul>
 * <p>
 * このコンポーネントはインスタンスモードPROTOTYPEで使われることを想定しており、スレッドセーフではありません。
 * </p>
 * 
 * @author koichik
 */
public interface MessageSender {

    /**
     * 送信するJMSメッセージの{@link javax.jms.DeliveryMode 配信モード}を設定します。デフォルトは{@link javax.jms.Message#DEFAULT_DELIVERY_MODE JMSメッセージのデフォルト配信モード}に従います。
     * 
     * @param deliveryMode
     *            送信するJMSメッセージの{@link javax.jms.DeliveryMode 配信モード}
     */
    void setDeliveryMode(int deliveryMode);

    /**
     * 送信するJMSメッセージの優先度を指定します。デフォルトは{@link javax.jms.Message#DEFAULT_PRIORITY JMSメッセージのデフォルト優先度}に従います。
     * 
     * @param priority
     *            送信するJMSメッセージの優先度
     */
    void setPriority(int priority);

    /**
     * 送信するJMSメッセージの生存時間をミリ秒単位で指定します。デフォルトは{@link javax.jms.Message#DEFAULT_TIME_TO_LIVE JMSメッセージのデフォルト生存時間}に従います。
     * 
     * @param timeToLive
     *            送信するJMSメッセージの生存時間(ミリ秒単位)
     */
    void setTimeToLive(long timeToLive);

    /**
     * 送信するJMSメッセージのメッセージIDを無効化する場合に{@code true}を設定します。デフォルトは{@code false}です。
     * 
     * @param disableMessageID
     *            送信するJMSメッセージのメッセージIDを無効化する場合は{@code true}、その他の場合は{@code false}
     */
    void setDisableMessageID(boolean disableMessageID);

    /**
     * 送信するJMSメッセージのタイムスタンプを無効化する場合に{@code true}を設定します。デフォルトは{@code false}です。
     * 
     * @param disableMessageTimestamp
     *            送信するJMSメッセージのタイムスタンプを無効化する場合は{@code true}、その他の場合は{@code false}
     */
    void setDisableMessageTimestamp(boolean disableMessageTimestamp);

    /**
     * JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSCorrelationID(String) correlationID}を文字列で設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setJMSCorrelationID(String)}でヘッダに設定されます。
     * </p>
     * 
     * @param correlationID
     *            JMSメッセージのヘッダに設定される
     *            {@link javax.jms.Message#setJMSCorrelationID(String) correlationID}
     */
    void setCorrelationID(String correlationID);

    /**
     * {@link javax.jms.Message#setJMSCorrelationIDAsBytes(byte[]) correlationID}をバイト列で設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])}でヘッダに設定されます。
     * </p>
     * 
     * @param correlationIDAsBytes
     *            JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSCorrelationIDAsBytes(byte[]) correlationID}
     */
    void setCorrelationIDAsBytes(byte[] correlationIDAsBytes);

    /**
     * {@link javax.jms.Message#setJMSReplyTo(Destination) replyTo}をバイト列で設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setJMSReplyTo(Destination)}でヘッダに設定されます。
     * </p>
     * 
     * @param replyTo
     *            JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSReplyTo(Destination) replyTo}
     */
    void setReplyTo(Destination replyTo);

    /**
     * 指定された名前を持つプロパティ値を設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setObjectProperty}でプロパティに設定されます。
     * </p>
     * 
     * @param name
     *            プロパティ名
     * @param value
     *            プロパティ値
     */
    void addProperty(String name, Object value);

    /**
     * 指定された{@link Map}に含まれるマッピングを全てプロパティとして設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setObjectProperty}でプロパティに設定されます。
     * </p>
     * 
     * @param properties
     *            プロパティ
     */
    void addProperties(Map<String, Object> properties);

    /**
     * バイト配列を{@link javax.jms.BytesMessage}のペイロードに設定して送信します。
     * 
     * @param bytes
     *            送信するバイト配列
     */
    void send(byte[] bytes);

    /**
     * 文字列を{@link javax.jms.TextMessage}のペイロードに設定して送信します。
     * 
     * @param text
     *            送信する文字列
     */
    void send(String text);

    /**
     * {@link java.io.Serializable}オブジェクトを{@link javax.jms.ObjectMessage}のペイロードに設定して送信します。
     * 
     * @param object
     *            送信するオブジェクト
     */
    void send(Serializable object);

    /**
     * {@link java.util.Map}を{@link javax.jms.MapMessage}のペイロードに設定して送信します。
     * 
     * @param map
     *            送信するマップ
     */
    void send(Map<String, Object> map);

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

    /**
     * 送信したJMSメッセージを返します。
     * <p>
     * JMSメッセージが送信されていない場合は<code>null</code>を返します．
     * </p>
     * 
     * @return 送信したJMSメッセージ
     */
    Message getMessage();

    /**
     * 送信したJMSメッセージの{@link javax.jms.Message#getJMSMessageID messageID}
     * ヘッダの値を返します。
     * 
     * @return 送信したJMSメッセージの{@link javax.jms.Message#getJMSMessageID messageID}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     * @throws EmptyRuntimeException
     *             JMSメッセージが送信されていない場合
     */
    String getMessageID();

    /**
     * 送信したJMSメッセージの{@link javax.jms.Message#getJMSTimestamp timestamp}
     * ヘッダの値を返します。
     * 
     * @return 送信したJMSメッセージの{@link javax.jms.Message#getJMSTimestamp timestamp}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     * @throws EmptyRuntimeException
     *             JMSメッセージが送信されていない場合
     */
    long getTimestamp();

    /**
     * 送信したJMSメッセージの{@link javax.jms.Message#getJMSExpiration expiration}
     * ヘッダの値を返します。
     * 
     * @return 送信したJMSメッセージの{@link javax.jms.Message#getJMSExpiration expiration}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     * @throws EmptyRuntimeException
     *             JMSメッセージが送信されていない場合
     */
    long getExpiration();

}
