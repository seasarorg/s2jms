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
package org.seasar.jms.core.mock;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * {@link javax.jms.TextMessage}のモックです。
 * 
 * @author koichik
 */
public class TextMessageMock extends MessageMock implements TextMessage {

    // instance fields
    /** JMSメッセージのペイロード */
    protected String text;

    /**
     * インスタンスを構築します。
     */
    public TextMessageMock() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param text
     *            JMSメッセージのペイロード
     */
    public TextMessageMock(final String text) {
        this.text = text;
    }

    public void setText(final String text) throws JMSException {
        this.text = text;
    }

    public String getText() throws JMSException {
        return text;
    }

}
