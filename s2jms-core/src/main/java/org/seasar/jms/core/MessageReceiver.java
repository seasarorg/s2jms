/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.jms.core.exception.SJMSRuntimeException;
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
 * いずれの場合もタイムアウトした場合は{@code null}を返します。
 * </p>
 * <p>
 * 受信したメッセージは{@link #getMessage()}メソッドで取得することもできます．
 * 受信したメッセージのJMSヘッダを以下のメソッドで取得することができます．
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
public interface MessageReceiver {

    /**
     * JMSメッセージを受信するまで待機する時間をミリ秒単位で設定します。
     * <p>
     * {@code timeout} < 0 の場合はJMSメッセージを受信するまで無制限に待機します。デフォルトです。<br>
     * {@code timeout} > 0 場合はその時間だけ待機します。<br>
     * {@code timeout} == 0 の場合は待機しません。
     * </p>
     * 
     * @param timeout
     *            JMSメッセージを受信するまで待機する時間 (ミリ秒単位)
     * @return このインスタンス自身
     */
    MessageReceiver setTimeout(long timeout);

    /**
     * 受信するJMSメッセージを選択するためのメッセージセレクタを指定します。 メッセージセレクタの詳細は{@link javax.jms.Message}を参照してください。
     * 
     * @param messageSelector
     *            受信するJMSメッセージを選択するためのメッセージセレクタ
     * @return このインスタンス自身
     */
    MessageReceiver setMessageSelector(String messageSelector);

    /**
     * 受信するJMSコネクションから送信されたJMSメッセージを受信しない場合は{@code true}に設定します。 デフォルトは{@code false}です。
     * JMSデスティネーションがトピックの場合にのみ有効です。
     * 
     * @param noLocal
     *            受信するJMSコネクションから送信されたJMSメッセージを受信しない場合は{@code true}、それ以外の場合は{@code false}
     * @return このインスタンス自身
     */
    MessageReceiver setNoLocal(boolean noLocal);

    /**
     * JMSメッセージをデュラブル(継続的)に受信する場合は{@code true}に設定します。デフォルトは{@code false}です。
     * JMSデスティネーションがトピックの場合にのみ有効です。
     * 
     * @param durable
     *            JMSメッセージをデュラブル(継続的)に受信する場合は{@code true}、それ以外の場合は{@code false}
     * @return このインスタンス自身
     */
    MessageReceiver setDurable(boolean durable);

    /**
     * JMSメッセージをデュラブル(継続的)に受信する場合のサブスクリプション名を設定します。
     * JMSデスティネーションがトピックで、JMSメッセージをデュラブルに受信する場合にのみ有効です。
     * 
     * @param subscriptionName
     *            JMSメッセージをデュラブル(継続的)に受信する場合のサブスクリプション名
     * @return このインスタンス自身
     */
    MessageReceiver setSubscriptionName(String subscriptionName);

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
     * @param <MSGTYPE>
     *            JMSメッセージの型
     * @param <T>
     *            JMSメッセージを処理した結果の型
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

    /**
     * 受信したJMSメッセージを返します。
     * <p>
     * JMSメッセージを受信していない場合は<code>null</code>を返します．
     * </p>
     * 
     * @return 受信したJMSメッセージ
     */
    Message getMessage();

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSMessageID messageID} ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSMessageID messageID}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     * @throws EmptyRuntimeException
     *             JMSメッセージを受信していない場合
     */
    String getMessageID();

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSTimestamp timestamp} ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSTimestamp timestamp}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     * @throws EmptyRuntimeException
     *             JMSメッセージを受信していない場合
     */
    long getTimestamp();

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSExpiration expiration}
     * ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSExpiration expiration}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     * @throws EmptyRuntimeException
     *             JMSメッセージを受信していない場合
     */
    long getExpiration();

}
