/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.jms.example.teeda.web.send.impl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.jms.core.MessageReceiver;
import org.seasar.jms.core.MessageSender;
import org.seasar.jms.example.teeda.web.send.MessagingService;

public class MessagingServiceImpl implements MessagingService {

    @Binding(bindingType = BindingType.MUST)
    protected MessageSender sender;

    @Binding(bindingType = BindingType.MUST)
    protected MessageReceiver receiver;

    public String send(final String text, final boolean needReply) {
        sender.addProperty("needReply", needReply);
        sender.send(text);
        return sender.getMessageID();
    }

    public String receive(final String id) {
        receiver.setMessageSelector("JMSCorrelationID = '" + id + "'");
        return receiver.receiveText();
    }

}
