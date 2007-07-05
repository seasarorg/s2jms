/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.util.concurrent.Callable;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.StringUtil;
import org.seasar.jms.container.external.JMSExternalContext;
import org.seasar.jms.container.external.JMSExternalContextComponentDefRegister;

/**
 * S2JMSコンテナを使用可能にするためにS2コンテナの初期化を行うクラスです。
 * 
 * @author koichik
 */
public class JMSContainerInitializer implements Callable<S2Container> {

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

    public S2Container call() {
        return initialize();
    }

    /**
     * S2コンテナを初期化します。
     * 
     * @return ルートのS2コンテナ
     */
    public S2Container initialize() {
        if (isAlreadyInitialized()) {
            return SingletonS2ContainerFactory.getContainer();
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
        return SingletonS2ContainerFactory.getContainer();
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
