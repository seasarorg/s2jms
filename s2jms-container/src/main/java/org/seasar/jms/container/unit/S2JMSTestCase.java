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
package org.seasar.jms.container.unit;

import javax.jms.Message;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ExternalContext;
import org.seasar.jms.container.external.JMSExternalContext;
import org.seasar.jms.container.external.JMSExternalContextComponentDefRegister;
import org.seasar.jms.container.external.JMSRequestImpl;

/**
 * S2JMS-Containerから呼び出されるメッセージリスナーコンポーネントをテストするための抽象テストクラスです。
 * <p>
 * このテストクラスを継承したテストクラスでは、 <code>setUp()</code>メソッドや<code>setUpXxx()</code>メソッドの中から{@link #registerMessage(javax.jms.Message)}メソッドを呼び出すことにより、
 * 外部コンテキストのリクエストにJMSメッセージを設定することができます。
 * </p>
 * 
 * @author y-komori
 * 
 */
public abstract class S2JMSTestCase extends S2TestCase {

    @Override
    protected void setUpContainer() throws Throwable {
        super.setUpContainer();
        final ExternalContext externalContext = new JMSExternalContext();
        getContainer().setExternalContext(externalContext);
        getContainer().setExternalContextComponentDefRegister(
                new JMSExternalContextComponentDefRegister());
    }

    /**
     * JMSメッセージを外部コンテキストに登録します。
     * 
     * @param message
     *            JMSメッセージ
     */
    protected void registerMessage(final Message message) {
        getContainer().getExternalContext().setRequest(new JMSRequestImpl(message));
    }

}
