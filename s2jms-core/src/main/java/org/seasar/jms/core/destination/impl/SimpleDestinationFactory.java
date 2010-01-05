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
package org.seasar.jms.core.destination.impl;

import javax.jms.Destination;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.jms.core.destination.DestinationFactory;

/**
 * {@link #setDestination destination}プロパティにJMSデスティネーション(キューまたはトピック)を保持するコンポーネントです。
 * <p>
 * このコンポーネントはインスタンスモードをSINGLETONに設定して使用することができます。<br>
 * {@link #setDestination destination}プロパティの設定にOGNL式を使う場合、そのOGNL式が
 * インスタンスモードPROTOTYPEやREQUESTのコンポーネントを使用する場合はこのコンポーネントの
 * インスタンスモードもPROTOTYPEやREQUESTに設定する必要があります。
 * </p>
 * 
 * @author koichik
 */
@Component(instance = InstanceType.SINGLETON)
public class SimpleDestinationFactory implements DestinationFactory {

    // instance fields
    /** デスティネーション */
    protected Destination destination;

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setDestination destination}プロパティの設定は必須となります。
     * </p>
     * 
     */
    public SimpleDestinationFactory() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param destination
     *            JMSデスティネーション
     */
    public SimpleDestinationFactory(final Destination destination) {
        this.destination = destination;
    }

    /**
     * JMSデスティネーションを返します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティの設定は必須です。
     * </p>
     * 
     * @return JMSデスティネーション
     */
    public Destination getDestination(final Session session) {
        return destination;
    }

    /**
     * JMSデスティネーションを設定します。
     * 
     * @param destination
     *            JMSデスティネーション
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDestination(final Destination destination) {
        this.destination = destination;
    }
}
