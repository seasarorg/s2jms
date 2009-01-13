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
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.tiger.CollectionsUtil;
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
 * @param <MSGTYPE>
 *            JMSメッセージの型
 * @author koichik
 */
public abstract class AbstractMessageFactory<MSGTYPE extends Message> implements
        MessageFactory<MSGTYPE> {

    // instance fields
    /** JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSCorrelationID(String) correlationID} */
    protected String correlationID;

    /** JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSCorrelationIDAsBytes(byte[]) correlationID} */
    protected byte[] correlationIDAsBytes;

    /** JMSメッセージのヘッダに設定される{@link javax.jms.Message#setJMSReplyTo(Destination) replyTo} */
    protected Destination replyTo;

    /** JMSメッセージのプロパティに設定される{@link java.util.Map} */
    protected final Map<String, Object> properties = CollectionsUtil.newHashMap();

    /**
     * インスタンスを構築します。
     * 
     */
    public AbstractMessageFactory() {
    }

    @Binding(bindingType = BindingType.MAY)
    public void setCorrelationID(final String correlationID) {
        this.correlationID = correlationID;
        this.correlationIDAsBytes = null;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setCorrelationIDAsBytes(final byte[] correlationIDAsBytes) {
        this.correlationIDAsBytes = correlationIDAsBytes;
        this.correlationID = null;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setReplyTo(Destination replyTo) {
        this.replyTo = replyTo;
    }

    public void addProperty(final String name, final Object value) {
        properties.put(name, value);
    }

    public void addProperties(final Map<String, Object> properties) {
        this.properties.putAll(properties);
    }

    public MSGTYPE createMessage(final Session session) {
        try {
            final MSGTYPE message = createMessageInstance(session);
            setupHeader(message);
            setupProperties(message);
            setupPayload(message);
            return message;
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
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
        if (replyTo != null) {
            message.setJMSReplyTo(replyTo);
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
