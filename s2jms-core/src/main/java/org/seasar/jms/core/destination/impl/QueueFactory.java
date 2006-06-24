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
package org.seasar.jms.core.destination.impl;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * JMSセッションから{@link javax.jms.Queue}を作成するコンポーネントです。
 * <p>
 * このコンポーネントはJMSセッションからデスティネーションを作成するため、通常JMSセッション毎に異なったインスタンスを生成する必要があります。
 * </p>
 * 
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class QueueFactory extends AbstractDestinationFactory {
    protected String name;

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setName name}プロパティの設定は必須となります。
     * </p>
     * 
     */
    public QueueFactory() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param name
     *            作成するJMSデスティネーション({@link javax.jms.Queue})の名前
     */
    public QueueFactory(final String name) {
        this.name = name;
    }

    /**
     * 作成するJMSデスティネーション({@link javax.jms.Queue})の名前を返します。
     * 
     * @return 作成するJMSデスティネーション({@link javax.jms.Queue})の名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 作成するJMSデスティネーション({@link javax.jms.Queue})の名前を設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティの設定は必須です。
     * </p>
     * 
     * @param name
     *            作成するJMSデスティネーション({@link javax.jms.Queue})の名前
     */
    @Binding(bindingType = BindingType.MAY)
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * JMSセッションからJMSデスティネーション({@link javax.jms.Queue})を作成して返します。
     * <p>
     * このメソッドは{@link org.seasar.jms.core.destination.impl.AbstractDestinationFactory#getDestination}が
     * 最初に呼び出された時に一度だけ呼び出されます。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSデスティネーション({@link javax.jms.Queue})
     */
    @Override
    protected Queue createDestination(final Session session) throws JMSException {
        return session.createQueue(name);
    }
}