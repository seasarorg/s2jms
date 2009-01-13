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

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.jms.core.text.TextProvider;
import org.seasar.jms.core.text.impl.TextHolder;

/**
 * {@link javax.jms.TextMessage}を作成するコンポーネントです。
 * <p>
 * このクラスはインスタンスモードPROTOTYPEで使われることを想定しており、スレッドセーフではありません。
 * </p>
 * 
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class TextMessageFactory extends AbstractMessageFactory<TextMessage> {

    // instance fields
    /** 受信したJMSメッセージのペイロード */
    protected TextProvider textProvider;

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setText text}プロパティまたは
     * {@link #setTextProvider textProvider}プロパティの設定は必須となります。
     * </p>
     * 
     */
    public TextMessageFactory() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param text
     *            JMSメッセージのペイロードに設定される文字列
     */
    public TextMessageFactory(final String text) {
        this(new TextHolder(text));
    }

    /**
     * インスタンスを構築します。
     * 
     * @param textProvider
     *            JMSメッセージのペイロードに設定される文字列を提供するプロバイダ
     */
    public TextMessageFactory(final TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    /**
     * JMSメッセージのペイロードに設定される文字列を返します。
     * 
     * @return JMSメッセージのペイロードに設定される文字列
     */
    public String getText() {
        return textProvider.getText();
    }

    /**
     * JMSメッセージのペイロードに設定される文字列を設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティまたは
     * {@link #setTextProvider textProvider}プロパティの設定は必須です。
     * </p>
     * 
     * @param text
     *            JMSメッセージのペイロードに設定される文字列
     */
    @Binding(bindingType = BindingType.MAY)
    public void setText(final String text) {
        this.textProvider = new TextHolder(text);
    }

    /**
     * JMSメッセージのペイロードに設定される文字列を提供するプロバイダを返します。
     * 
     * @return JMSメッセージのペイロードに設定される文字列を提供するプロバイダ
     */
    public TextProvider getTextProvider() {
        return textProvider;
    }

    /**
     * JMSメッセージのペイロードに設定される文字列を提供するプロバイダを設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティまたは {@link #setText text}プロパティの設定は必須です。
     * </p>
     * 
     * @param textProvider
     *            JMSメッセージのペイロードに設定される文字列を提供するプロバイダ
     */
    @Binding(bindingType = BindingType.MAY)
    public void setTextProvider(final TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    /**
     * JMSセッションから{@link javax.jms.TextMessage}を作成して返します。
     * 
     * @param session
     *            JMSセッション
     * @return JMSセッションから作成された{@link javax.jms.TextMessage}
     */
    @Override
    protected TextMessage createMessageInstance(final Session session) throws JMSException {
        return session.createTextMessage();
    }

    /**
     * JMSペイロードに{@link #setText text}プロパティの値を設定します。
     * 
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             JMSメッセージにペイロードを設定できなかった場合にスローされます
     */
    @Override
    protected void setupPayload(final TextMessage message) throws JMSException {
        message.setText(getText());
    }

}
