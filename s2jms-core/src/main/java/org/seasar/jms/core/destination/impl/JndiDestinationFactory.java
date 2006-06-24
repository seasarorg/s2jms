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

import java.util.Hashtable;

import javax.jms.Destination;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.seasar.extension.j2ee.JndiContextFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.jms.core.exception.SJMSRuntimeException;

/**
 * JNDIからJMSデスティネーション(キューまたはトピック)を取得するコンポーネントです。
 * <p>
 * このコンポーネントはインスタンスモードをSINGLETONに設定して使用することができます。
 * </p>
 * 
 * @author koichik
 */
@Component(instance = InstanceType.SINGLETON)
public class JndiDestinationFactory extends AbstractDestinationFactory {
    protected Hashtable<String, Object> env;
    protected String name;

    /**
     * インスタンスを構築します。
     * <p>
     * このコンストラクタでインスタンスを構築した場合、{@link #setEnv env}プロパティおよび
     * {@link #setName name}プロパティの設定は必須となります。
     * </p>
     * 
     */
    public JndiDestinationFactory() {
        env = new Hashtable<String, Object>();
        env.put(InitialContext.INITIAL_CONTEXT_FACTORY, JndiContextFactory.class.getName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param env
     *            初期コンテキストの作成に使用される環境。{@code null}は空の環境を示す
     * @param name
     *            ルックアップするJMSデスティネーションの名前
     */
    public JndiDestinationFactory(final Hashtable<String, Object> env, final String name) {
        this.env = env;
        this.name = name;
    }

    /**
     * 初期コンテキストの作成に使用される環境を返します。
     * 
     * @return 初期コンテキストの作成に使用される環境
     */
    public Hashtable getEnv() {
        return this.env;
    }

    /**
     * 初期コンテキストの作成に使用される環境を設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティの設定は必須です。
     * </p>
     * 
     * @param env
     *            初期コンテキストの作成に使用される環境
     */
    @Binding(bindingType = BindingType.MAY)
    public void setEnv(final Hashtable<String, Object> env) {
        this.env = env;
    }

    /**
     * ルックアップするJMSデスティネーションの名前を返します。
     * 
     * @return ルックアップするJMSデスティネーションの名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * ルックアップするJMSデスティネーションの名前を設定します。
     * <p>
     * デフォルトコンストラクタでインスタンスを構築した場合、このプロパティの設定は必須です。
     * </p>
     * 
     * @param name
     *            ルックアップするJMSデスティネーションの名前
     */
    @Binding(bindingType = BindingType.MAY)
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * JNDIからJMSデスティネーションをルックアップして返します。
     * <p>
     * このメソッドは{@link org.seasar.jms.core.destination.impl.AbstractDestinationFactory#getDestination}が
     * 最初に呼び出された時に一度だけ呼び出されます。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSデスティネーション {@code null}が返されることはありません
     * @throws SJMSRuntimeException
     *             JNDIの操作で例外が発生した場合や{@link setName name}プロパティに設定された名前が
     *             見つからなかった場合にスローされます
     */
    @Override
    protected Destination createDestination(final Session session) {
        try {
            final InitialContext context = new InitialContext(env);
            try {
                return (Destination) context.lookup(name);
            } finally {
                context.close();
            }
        } catch (final NamingException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }
}
