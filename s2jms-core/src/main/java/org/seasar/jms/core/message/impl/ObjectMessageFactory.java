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
package org.seasar.jms.core.message.impl;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * {@link javax.jms.ObjectMessage}を作成するコンポーネントです。
 * <p>
 * このクラスはインスタンスモードPROTOTYPEで使われることを想定しており、スレッドセーフではありません。
 * </p>
 * 
 * @author bowez
 */
@Component(instance = InstanceType.PROTOTYPE)
public class ObjectMessageFactory extends AbstractMessageFactory<ObjectMessage> {

    // instance fields
    /** 受信したJMSメッセージのペイロード */
    protected Serializable object;

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setObject object}プロパティの設定は必須となります。
     * </p>
     * 
     */
    public ObjectMessageFactory() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param object
     *            JMSメッセージのペイロードに設定されるオブジェクト
     */
    public ObjectMessageFactory(final Serializable object) {
        this.object = object;
    }

    /**
     * JMSメッセージのペイロードに設定されるオブジェクトを返します。
     * 
     * @return JMSメッセージのペイロードに設定されるオブジェクト
     */
    public Serializable getObject() {
        return this.object;
    }

    /**
     * JMSメッセージのペイロードに設定されるオブジェクトを設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティの設定は必須です。
     * </p>
     * 
     * @param object
     *            JMSメッセージのペイロードに設定されるオブジェクト
     */
    @Binding(bindingType = BindingType.MAY)
    public void setObject(final Serializable object) {
        this.object = object;
    }

    /**
     * JMSセッションから{@link javax.jms.ObjectMessage}を作成して返します。
     * 
     * @param session
     *            JMSセッション
     * @return JMSセッションから作成された{@link javax.jms.ObjectMessage}
     */
    @Override
    protected ObjectMessage createMessageInstance(final Session session) throws JMSException {
        return session.createObjectMessage();
    }

    /**
     * JMSペイロードに{@link #setObject object}プロパティの値を設定します。
     * 
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             JMSメッセージにペイロードを設定できなかった場合にスローされます
     */
    @Override
    protected void setupPayload(final ObjectMessage message) throws JMSException {
        message.setObject(object);
    }

}
