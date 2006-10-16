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

import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * {@link javax.jms.BytesMessage}を作成するコンポーネントです。
 * <p>
 * このクラスはインスタンスモードPROTOTYPEで使われることを想定しており、スレッドセーフではありません。
 * </p>
 * 
 * @author bowez
 */
@Component(instance = InstanceType.PROTOTYPE)
public class BytesMessageFactory extends AbstractMessageFactory<BytesMessage> {
    protected byte[] bytes;

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setBytes bytes}プロパティの設定は必須となります。
     * </p>
     * 
     */
    public BytesMessageFactory() {
    }

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setBytes bytes}プロパティの設定は必須となります。
     * </p>
     * 
     * @param properties
     *            JMSメッセージのプロパティに設定される{@link java.util.Map}
     */
    public BytesMessageFactory(final Map<String, Object> properties) {
        super(properties);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param bytes
     *            JMSメッセージのペイロードに設定されるバイト列
     */
    public BytesMessageFactory(final byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param bytes
     *            JMSメッセージのペイロードに設定されるバイト列
     * @param properties
     *            JMSメッセージのプロパティに設定される{@link java.util.Map}
     */
    public BytesMessageFactory(final byte[] bytes, final Map<String, Object> properties) {
        super(properties);
        this.bytes = bytes;
    }

    /**
     * JMSメッセージのペイロードに設定されるバイト列を返します。
     * 
     * @return JMSメッセージのペイロードに設定されるバイト列
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * JMSメッセージのペイロードに設定されるバイト列を設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティの設定は必須です。
     * </p>
     * 
     * @param bytes
     *            JMSメッセージのペイロードに設定されるバイト列
     */
    @Binding(bindingType = BindingType.MAY)
    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * JMSセッションから{@link javax.jms.BytesMessage}を作成して返します。
     * 
     * @param session
     *            JMSセッション
     * @return JMSセッションから作成された{@link javax.jms.BytesMessage}
     */
    @Override
    protected BytesMessage createMessageInstance(final Session session) throws JMSException {
        return session.createBytesMessage();
    }

    /**
     * JMSペイロードに{@link #setBytes bytes}プロパティの値を設定します。
     * 
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             JMSメッセージにペイロードを設定できなかった場合にスローされます
     */
    @Override
    protected void setupPayload(final BytesMessage message) throws JMSException {
        message.writeBytes(bytes);
    }
}
