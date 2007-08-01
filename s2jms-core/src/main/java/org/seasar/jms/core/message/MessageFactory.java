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
package org.seasar.jms.core.message;

import java.util.Map;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Session;

/**
 * JMSメッセージを作成するコンポーネントのインタフェースです。
 * 
 * @param <MSGTYPE>
 *            JMSメッセージの型
 * @author koichik
 */
public interface MessageFactory<MSGTYPE extends Message> {

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
    void setCorrelationID(final String correlationID);

    /**
     * {@link javax.jms.Message#setJMSCorrelationIDAsBytes(byte[]) correlationID}をバイト列で設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])}でヘッダに設定されます。
     * </p>
     * 
     * @param correlationIDAsBytes
     *            JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSCorrelationIDAsBytes(byte[]) correlationID}
     */
    void setCorrelationIDAsBytes(final byte[] correlationIDAsBytes);

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
    void addProperty(final String name, final Object value);

    /**
     * 指定された{@link Map}に含まれるマッピングを全てプロパティとして設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setObjectProperty}でプロパティに設定されます。
     * </p>
     * 
     * @param properties
     *            プロパティ
     */
    void addProperties(final Map<String, Object> properties);

    /**
     * JMSセッションからJMSメッセージを作成して返します。
     * <p>
     * 作成されたJMSメッセージのヘッダおよびプロパティはこのコンポーネントからコピーされます。 JMSメッセージのペイロードは実装クラスの{@code setupPayload()}メソッドで設定されます。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSメッセージ
     */
    MSGTYPE createMessage(Session session);

}
