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
package org.seasar.jms.core.session.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

/**
 * JMSセッションを作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class SessionFactoryImpl implements SessionFactory {
    protected ConnectionFactory connectionFactory;
    protected boolean transacted = true;
    protected int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;

    /**
     * インスタンスを構築します。
     */
    public SessionFactoryImpl() {
    }

    /**
     * JMSコネクションファクトリを設定します(必須)。
     * 
     * @param connectionFactory
     *            JMSコネクションファクトリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setConnectionFactory(final ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * JMSメッセージをトランザクショナルに受信する場合は{@code true}を設定します。デフォルトは{@code true}です。
     * 
     * @param transacted
     *            JMSメッセージをトランザクショナルに受信する場合は{@code true}、それ以外の場合は{@code false}
     */
    @Binding(bindingType = BindingType.MAY)
    public void setTransacted(final boolean transacted) {
        this.transacted = transacted;
    }

    /**
     * Acknowledge(応答)モードを設定します。デフォルトは{@link javax.jms.Session#AUTO_ACKNOWLEDGE}です。
     * 
     * @param acknowledgeMode
     *            Acknowledge(応答)モード
     */
    @Binding(bindingType = BindingType.MAY)
    public void setAcknowledgeMode(final int acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    /**
     * {@link setConnectionFactory connectionFactory}プロパティに設定された{@link javax.jms.ConnectionFactory}を使用して
     * JMSコネクションを作成します。
     * <p>
     * 作成したJMSコネクションが{@link #processConnection}メソッドで処理された後、JMSコネクションはクローズされます。
     * </p>
     * 
     * @param startConnection
     *            JMSセッションを作成する前に{@link javax.jms.Connection#start()}を呼び出す必要がある場合は{@code true}、それ以外の場合は{@code false}
     * @param handler
     *            JMSセッションを処理するハンドラ
     * @throws SJMSRuntimeException
     *             {@link javax.jms.JMSException}が発生した場合にスローされます
     */
    public void operateSession(final boolean startConnection, final SessionHandler handler) {
        try {
            final Connection connection = connectionFactory.createConnection();
            try {
                processConnection(startConnection, handler, connection);
            } finally {
                connection.close();
            }
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }

    /**
     * JMSコネクションからJMSセッションを作成します。
     * <p>
     * 作成したJMSセッションが{@link org.seasar.jms.core.session.SessionHandler#handleSession}メソッドで処理された後、JMSセッションはクローズされます。<br>
     * 引数{@code startConnection}に{@code true}が指定された場合は、JMSセッションを作成する前に{@link javax.jms.Connection#start()}が、
     * JMSセッションがクローズされた後に{@link javax.jms.Connection#stop}が呼び出されます。
     * </p>
     * 
     * @param startConnection
     *            JMSセッションを作成する前に{@link javax.jms.Connection#start()}を呼び出す必要がある場合は{@code true}、それ以外の場合は{@code false}
     * @param handler
     *            JMSセッションを処理するハンドラ
     * @param connection
     *            JMSコネクション
     * @throws JMSException
     *             JMS実装で例外が発生した場合にスローされます
     */
    protected void processConnection(final boolean startConnection, final SessionHandler handler,
            final Connection connection) throws JMSException {
        if (startConnection) {
            connection.start();
        }
        try {
            final Session session = connection.createSession(transacted, acknowledgeMode);
            try {
                handler.handleSession(session);
            } finally {
                session.close();
            }
        } finally {
            if (startConnection) {
                connection.stop();
            }
        }
    }
}
