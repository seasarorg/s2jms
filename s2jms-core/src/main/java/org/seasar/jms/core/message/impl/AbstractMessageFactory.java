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

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.message.MessageFactory;

/**
 * JMSメッセージを作成するコンポーネントの抽象クラスです。
 * <p>
 * このコンポーネントはJMSセッションからJMSメッセージを作成します。作成されるJMSメッセージの型は型引数として示されます。
 * 作成されたJMSメッセージのヘッダやプロパティはこのコンポーネントのプロパティに設定されているものがコピーされます。
 * JMSメッセージを生成する方法とそのペイロードを設定する方法はサブクラスに依存します。
 * </p>
 * <p>
 * このクラスおよびサブクラスはインスタンスモードPROTOTYPEで使われることを想定しており、スレッドセーフではありません。
 * </p>
 * 
 * @author koichik
 */
public abstract class AbstractMessageFactory<MSGTYPE extends Message> implements
        MessageFactory<MSGTYPE> {
    protected String correlationID;
    protected byte[] correlationIDAsBytes;
    protected Map<String, Object> properties = new HashMap<String, Object>();

    /**
     * インスタンスを構築します。
     * 
     */
    public AbstractMessageFactory() {
    }

    /**
     * JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSCorrelationID correlationID}を文字列で返します。
     * 
     * @return JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSCorrelationID correlationID}
     */
    public String getCorrelationID() {
        return correlationID;
    }

    /**
     * JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSCorrelationID correlationID}を文字列で設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setJMSCorrelationID}でヘッダに設定されます。
     * </p>
     * 
     * @param JMSメッセージのヘッダに設定される
     *            {@link javax.jms.Message#setJMSCorrelationID correlationID}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setCorrelationID(final String correlationID) {
        this.correlationID = correlationID;
        this.correlationIDAsBytes = null;
    }

    /**
     * JMSメッセージのヘッダに設定される{@link javax.jms.Message#getJMSCorrelationIDAsBytes correlationID}をバイト列で返します。
     * 
     * @return JMSメッセージのヘッダに設定される{@link javax.jms.Message#getJMSCorrelationIDAsBytes correlationID}
     */
    public byte[] getCorrelationIDAsBytes() {
        return correlationIDAsBytes;
    }

    /**
     * {@link javax.jms.Message#getJMSCorrelationIDAsBytes correlationID}をバイト列で設定します。
     * <p>
     * 設定された値は作成されたJMSメッセージの{@link javax.jms.Message#setJMSCorrelationIDAsBytes}でヘッダに設定されます。
     * </p>
     * 
     * @param correlationIdAsBytes
     *            JMSメッセージのヘッダに設定される{@link javax.jms.Message#getJMSCorrelationIDAsBytes correlationID}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setCorrelationIDAsBytes(final byte[] correlationIDAsBytes) {
        this.correlationIDAsBytes = correlationIDAsBytes;
        this.correlationID = null;
    }

    /**
     * 指定された名前を持つプロパティ値を返します。
     * 
     * @param name
     *            プロパティ名
     * @return プロパティ名に対応するプロパティ値
     */
    public Object getProperty(final String name) {
        return properties.get(name);
    }

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
    public void addProperty(final String name, final Object value) {
        properties.put(name, value);
    }

    /**
     * JMSセッションからJMSメッセージを作成して返します。
     * <p>
     * 作成されたJMSメッセージのヘッダおよびプロパティはこのコンポーネントからコピーされます。
     * JMSメッセージのペイロードはサブクラスによって実装される{@link #setupPayload}で設定されます。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSメッセージ
     */
    public MSGTYPE createMessage(final Session session) {
        try {
            final MSGTYPE message = createMessageInstance(session);
            setupHeader(message);
            setupProperties(message);
            setupPayload(message);
            return message;
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    /**
     * JMSメッセージのインスタンスを作成して返します。
     * <p>
     * サブクラス固有の方法でJMSメッセージを作成します。 作成されたJMSメッセージの型は型引数{@code MSGTYPE}に
     * 適合しなくてはなりません。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSメッセージ
     * @throws JMSException
     *             JMSメッセージを作成できなかった場合にスローされます。
     */
    protected abstract MSGTYPE createMessageInstance(final Session session) throws JMSException;

    /**
     * JMSメッセージのメッセージヘッダを設定します。
     * <p>
     * このインスタンスのプロパティの値をJMSメッセージのヘッダに設定します。<br>
     * 現在対応しているヘッダは{@link javax.jms.Message#setJMSCorrelationID}および
     * {@link javax.jms.Message#setJMSCorrelationIDAsBytes}だけです。
     * </p>
     * 
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             JMSメッセージにヘッダを設定できなかった場合にスローされます
     */
    protected void setupHeader(final Message message) throws JMSException {
        if (correlationID != null) {
            message.setJMSCorrelationID(correlationID);
        } else if (correlationIDAsBytes != null) {
            message.setJMSCorrelationIDAsBytes(correlationIDAsBytes);
        }
    }

    /**
     * JMSメッセージのメッセージプロパティを設定します。
     * <p>
     * このインスタンスに{@link #addProperty}で設定された値をJMSメッセージのプロパティに設定します。
     * </p>
     * 
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             JMSメッセージにプロパティを設定できなかった場合にスローされます
     */
    protected void setupProperties(final Message message) throws JMSException {
        for (final Map.Entry<String, Object> entry : properties.entrySet()) {
            message.setObjectProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * JMSメッセージのペイロードを設定します。
     * 
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             JMSメッセージにペイロードを設定できなかった場合にスローされます
     */
    protected abstract void setupPayload(MSGTYPE message) throws JMSException;
}