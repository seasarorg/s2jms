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
package org.seasar.jms.container;

import javax.jms.MessageListener;

/**
 * S2JMSContainerが実装するインターフェースです。
 * 
 * @author y-komori
 */
public interface JMSContainer extends MessageListener {

    /**
     * S2JMSContainerにメッセージリスナ・コンポーネントを登録します。<br>
     * メッセージリスナ・コンポーネントはDiconファイルに登録し、通常以下のように使用します。<br>
     * 
     * <pre>
     *      &lt;component class=&quot;org.seasar.jms.container.impl.JMSContainerImpl&quot;&gt;
     *      &lt;initMethod name=&quot;addMessageListener&quot;&gt;
     *          &lt;arg&gt;&quot;paymentActionJms&quot;&lt;/arg&gt;
     *      &lt;/initMethod&gt;
     * </pre>
     * 
     * @param messageListenerName
     *            Diconに定義されたmessageListenerクラスのコンポーネント名。
     */
    public void addMessageListener(String messageListenerName);

}
