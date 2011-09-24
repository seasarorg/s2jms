/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.jms.container.impl;

import java.lang.reflect.Method;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

import org.seasar.framework.util.ClassUtil;
import org.seasar.jca.inbound.AbstractMessageEndpoint;

/**
 * JMS用の{@link MessageEndpoint}の実装クラスです．
 * 
 * @author koichik
 */
public class JMSMessageEndpoint extends AbstractMessageEndpoint implements MessageListener {

    /** {@link MessageListener#onMessage(Message)}を表すメソッドオブジェクト */
    public static final Method LISTENER_METHOD = ClassUtil.getMethod(MessageListener.class,
            "onMessage", new Class[] { Message.class });

    /** 移譲先となる本来のメッセージエンドポイント */
    public MessageListener actualEndpoint;

    /**
     * インスタンスを構築します．
     * 
     * @param messageEndpointFactory
     *            メッセージエンドポイントファクトリ
     * @param transactionManager
     *            トランザクションマネージャ
     * @param xaResource
     *            XAリソース
     * @param classLoader
     *            クラスローダ
     * @param actualEndpoint
     *            移譲先となる本来のメッセージエンドポイント
     */
    public JMSMessageEndpoint(final MessageEndpointFactory messageEndpointFactory,
            final TransactionManager transactionManager, final XAResource xaResource,
            final ClassLoader classLoader, final MessageListener actualEndpoint) {
        super(messageEndpointFactory, transactionManager, xaResource, classLoader);
        this.actualEndpoint = actualEndpoint;
    }

    public void onMessage(final Message message) {
        delivery(message);
    }

    @Override
    protected Object deligateActualEndpoint(Object message) {
        actualEndpoint.onMessage(Message.class.cast(message));
        return null;
    }

    @Override
    protected Method getListenerMethod() {
        return LISTENER_METHOD;
    }

}
