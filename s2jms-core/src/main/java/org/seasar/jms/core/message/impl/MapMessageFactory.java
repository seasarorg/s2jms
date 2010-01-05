/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
import javax.jms.MapMessage;
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
public class MapMessageFactory extends AbstractMessageFactory<MapMessage> {

    // instance fields
    /** 受信したJMSメッセージのペイロード */
    protected Map<String, Object> map;

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setMap map}プロパティの設定は必須となります。
     * </p>
     * 
     */
    public MapMessageFactory() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param map
     *            JMSメッセージのペイロードに設定される{@link java.util.Map}
     */
    public MapMessageFactory(final Map<String, Object> map) {
        this.map = map;
    }

    /**
     * JMSメッセージのペイロードに設定される{@link java.util.Map}を返します。
     * 
     * @return JMSメッセージのペイロードに設定される{@link java.util.Map}
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * JMSメッセージのペイロードに設定される{@link java.util.Map}を設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティの設定は必須です。
     * </p>
     * 
     * @param map
     *            JMSメッセージのペイロードに設定される{@link java.util.Map}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setMap(final Map<String, Object> map) {
        this.map = map;
    }

    /**
     * JMSメッセージのペイロードに設定される{@link java.util.Map}にキーと値のマッピングを追加します。
     * 
     * @param key
     *            JMSメッセージのペイロードに設定されるマッピングのキー
     * @param value
     *            JMSメッセージのペイロードに設定されるマッピングの値
     */
    public void addValue(final String key, final Object value) {
        if (this.map == null) {
            this.map = new HashMap<String, Object>();
        }
        this.map.put(key, value);
    }

    /**
     * JMSセッションから{@link javax.jms.MapMessage}を作成して返します。
     * 
     * @param session
     *            JMSセッション
     * @return JMSセッションから作成された{@link javax.jms.MapMessage}
     */
    @Override
    protected MapMessage createMessageInstance(final Session session) throws JMSException {
        return session.createMapMessage();
    }

    /**
     * JMSペイロードに{@link #setMap map}プロパティの値を設定します。
     * 
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             JMSメッセージにペイロードを設定できなかった場合にスローされます
     */
    @Override
    protected void setupPayload(final MapMessage message) throws JMSException {
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            message.setObject(entry.getKey(), entry.getValue());
        }
    }

}
