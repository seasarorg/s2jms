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
package org.seasar.jms.core.message.impl;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 受信した{@link javax.jms.TextMessage}を処理するコンポーネントです。
 * <p>
 * このクラスはインスタンスモードPROTOTYPEで使われることを想定しており、スレッドセーフではありません。
 * </p>
 * 
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class TextMessageHandler extends AbstractMessageHandler<TextMessage, String> {

    /**
     * インスタンスを構築します。
     * 
     */
    public TextMessageHandler() {
    }

    @Override
    public String getPayload() throws JMSException {
        return getMessage().getText();
    }

    public Class<TextMessage> getMessageType() {
        return TextMessage.class;
    }

    public Class<String> getPayloadType() {
        return String.class;
    }

}
