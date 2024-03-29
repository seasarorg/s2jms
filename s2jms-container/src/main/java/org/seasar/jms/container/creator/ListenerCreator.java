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
package org.seasar.jms.container.creator;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * JMSメッセージを処理するリスナコンポーネント (Listener) 用のクリエータです。
 * 
 * @author koichik
 * 
 */
public class ListenerCreator extends ComponentCreatorImpl {

    // constants
    /** リスナコンポーネントのデフォルト・サフィックス */
    public static final String DEFAULT_LISTENER_SUFFIX = "Listener";

    /**
     * インスタンスを構築します。
     * 
     * @param namingConvention
     *            ネーミングコンベンション
     */
    public ListenerCreator(final NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(DEFAULT_LISTENER_SUFFIX);
        setInstanceDef(InstanceDefFactory.REQUEST);
    }

    /**
     * Listener用のカスタマイザを返します。
     * 
     * @return Listener用のカスタマイザ
     */
    public ComponentCustomizer getListenerCustomizer() {
        return getCustomizer();
    }

    /**
     * Listener用のカスタマイザを設定します。
     * 
     * @param customizer
     *            Listener用のカスタマイザ
     */
    public void setListenerCustomizer(final ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }

}
