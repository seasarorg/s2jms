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

import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.util.MessageHandlerUtil;

/**
 * 受信したJMSメッセージを処理するコンポーネントの抽象クラスです。
 * <p>
 * 受信したJMSメッセージは{@link #handleMessage}で受け取りインスタンスフィールドで保持します。
 * 本クラスは受信したJMSメッセージのヘッダおよびプロパティにアクセスするメソッドを提供します。
 * </p>
 * <p>
 * このクラスおよびサブクラスはインスタンスモードPROTOTYPEで使われることを想定しており、スレッドセーフではありません。
 * </p>
 * 
 * @param <MSGTYPE>
 *            JMSメッセージの型
 * @param <PAYLOADTYPE>
 *            JMSメッセージのペイロードの型
 * @author koichik
 */
public abstract class AbstractMessageHandler<MSGTYPE extends Message, PAYLOADTYPE> implements
        MessageHandler<MSGTYPE, PAYLOADTYPE> {

    // instance fields
    /** 受信したJMSメッセージ */
    protected MSGTYPE message;

    /**
     * インスタンスを構築します。
     * 
     */
    public AbstractMessageHandler() {
    }

    /**
     * JMSメッセージを処理してペイロードを返します。
     * 
     * @param message
     *            受信したJMSメッセージ
     * @return JMSメッセージのペイロード
     */
    public PAYLOADTYPE handleMessage(final MSGTYPE message) {
        try {
            this.message = message;
            return getPayload();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージのペイロードを返します。
     * 
     * @return JMSメッセージのペイロード
     * @throws JMSException
     *             ペイロードを取得できなかった場合にスローされます
     */
    public abstract PAYLOADTYPE getPayload() throws JMSException;

    /**
     * JMSメッセージを返します。
     * 
     * @return JMSメッセージ
     */
    public MSGTYPE getMessage() {
        return message;
    }

    /**
     * 受信したJMSメッセージのAcknowledge(応答)を返します。
     * 
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public void acknowledge() {
        try {
            message.acknowledge();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSCorrelationID correlationID}
     * ヘッダの値を文字列で返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#setJMSCorrelationID correlationID}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public String getCorrelationID() {
        try {
            return message.getJMSCorrelationID();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSCorrelationIDAsBytes correlationID}
     * ヘッダの値をバイト列で返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSCorrelationIDAsBytes correlationID}ヘッダの値のバイト列
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public byte[] getCorrelationIDAsBytes() {
        try {
            return message.getJMSCorrelationIDAsBytes();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSDeliveryMode deliveryMode}
     * ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSDeliveryMode deliveryMode}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public int getDeliveryMode() {
        try {
            return message.getJMSDeliveryMode();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSDestination destination}
     * ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSDestination destination}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public Destination getDestination() {
        try {
            return message.getJMSDestination();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSExpiration expiration}
     * ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSExpiration expiration}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public long getExpiration() {
        try {
            return message.getJMSExpiration();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSMessageID messageID} ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSMessageID messageID}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public String getMessageID() {
        try {
            return message.getJMSMessageID();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSPriority priority} ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSPriority priority}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public int getPriority() {
        try {
            return message.getJMSPriority();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSRedelivered redelivered}
     * ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSRedelivered redelivered}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public boolean getRedelivered() {
        try {
            return message.getJMSRedelivered();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSReplyTo replyTo} ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSReplyTo replyTo}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public Destination getReplyTo() {
        try {
            return message.getJMSReplyTo();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの{@link javax.jms.Message#getJMSTimestamp timestamp} ヘッダの値を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSTimestamp timestamp}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public long getTimestamp() {
        try {
            return message.getJMSTimestamp();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージの型({@link javax.jms.Message#getJMSType}の戻り値)を返します。
     * 
     * @return JMSメッセージの{@link javax.jms.Message#getJMSTimestamp timestamp}ヘッダの値
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public String getType() {
        try {
            return message.getJMSType();
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    /**
     * JMSメッセージのプロパティを{@link java.util.Map}で返します。
     * 
     * @return JMSメッセージのプロパティ
     * @throws SJMSRuntimeException
     *             JMS実装で例外が発生した場合にスローされます
     */
    public Map<String, Object> getProperties() {
        return MessageHandlerUtil.getProperties(message);
    }

}
