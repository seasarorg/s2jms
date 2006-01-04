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
import javax.jms.Session;
import javax.jms.TextMessage;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.jms.core.message.text.TextHolder;
import org.seasar.jms.core.message.text.TextProvider;

/**
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class TextMessageFactory extends AbstractMessageFactory<TextMessage> {
    protected TextProvider textProvider;

    public TextMessageFactory() {
    }

    public TextMessageFactory(final String text) {
        this(new TextHolder(text));
    }

    public TextMessageFactory(final TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setTextProvider(final TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    @Override
    protected TextMessage createMessageInstance(final Session session) throws JMSException {
        return session.createTextMessage();
    }

    @Override
    protected void setupBody(final TextMessage message) throws JMSException {
        message.setText(textProvider.getText());
    }
}
