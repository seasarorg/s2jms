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

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StringUtil;
import org.seasar.jca.deploy.impl.MessageEndpointActivator;
import org.seasar.jms.container.external.JMSExternalContext;
import org.seasar.jms.container.external.JMSExternalContextComponentDefRegister;

/**
 * S2JMSコンテナを使用可能にするためにS2コンテナの初期化を行うクラスです。
 * 
 * @author koichik
 */
public class JMSContainerInitializer {

    // static fields
    private static final Logger logger = Logger.getLogger(JMSContainerInitializer.class);

    // instance fields
    /** S2コンテナを作成するための設定ファイルのパス */
    protected String configPath;

    /**
     * インスタンスを構築します。
     * 
     * @param configPath
     *            S2コンテナを作成するための設定ファイルのパス
     */
    public JMSContainerInitializer(final String configPath) {
        this.configPath = configPath;
    }

    /**
     * S2コンテナを初期化します。
     */
    public void initialize() {
        if (isAlreadyInitialized()) {
            return;
        }
        if (!StringUtil.isEmpty(configPath)) {
            SingletonS2ContainerFactory.setConfigPath(configPath);
        }
        if (ComponentDeployerFactory.getProvider() instanceof ComponentDeployerFactory.DefaultProvider) {
            ComponentDeployerFactory.setProvider(new ExternalComponentDeployerProvider());
        }
        SingletonS2ContainerFactory.setExternalContext(new JMSExternalContext());
        SingletonS2ContainerFactory
                .setExternalContextComponentDefRegister(new JMSExternalContextComponentDefRegister());
        SingletonS2ContainerFactory.init();
    }

    /**
     * S2コンテナを終了します。
     * 
     * <p>
     * S2Containerの終了は初期化とは逆順に、上位コンテナに登録された末尾のコンポーネントから終了します。
     * そのため、S2JMSのメッセージエンドポイントが終了するよりも先にCOOL deployで自動登録された、
     * アプリケーションのコンポーネントが終了してしまい、S2JMS-Containerがメッセージを
     * アプリケーションにディスパッチすることができなくなってしまいます。
     * 
     * S2JMS-Serverは、S2コンテナを終了する前にS2JMSのメッセージエンドポイントを終了しなければなりません。
     * </p>
     */
    public void destroy() {
        if (!isAlreadyInitialized()) {
            return;
        }
        final S2Container container = SingletonS2ContainerFactory.getContainer();
        final MessageEndpointActivator[] activators = (MessageEndpointActivator[]) container
                .findAllComponents(MessageEndpointActivator.class);
        for (int i = 0; i < activators.length; ++i) {
            try {
                activators[i].stop();
            } catch (final Exception e) {
                logger.log("EJMS-CONTAINER0001", new Object[] { e }, e);
            }
        }
        S2ContainerFactory.destroy();
    }

    /**
     * すでに初期化されていれば<code>true</code>を返します。
     * 
     * @return すでに初期化されていれば<code>true</code>
     */
    protected boolean isAlreadyInitialized() {
        return SingletonS2ContainerFactory.hasContainer();
    }

}
