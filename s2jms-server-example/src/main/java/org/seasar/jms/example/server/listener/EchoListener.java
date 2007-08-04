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
package org.seasar.jms.example.server.listener;

import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.annotation.JMSHeader;
import org.seasar.jms.container.annotation.JMSPayload;
import org.seasar.jms.container.annotation.JMSProperty;
import org.seasar.jms.container.annotation.OnMessage;
import org.seasar.jms.core.MessageSender;

/**
 * @author koichik
 */
public class EchoListener {

    private static Logger logger = Logger.getLogger(EchoListener.class);

    @Binding(bindingType = BindingType.MUST)
    protected MessageSender messageSender;

    @JMSHeader
    protected String messageID;

    @JMSProperty
    protected Map<String, Object> properties;

    @JMSProperty
    protected boolean needReply;

    @JMSPayload
    protected String text;

    public EchoListener() {
    }

    @OnMessage
    public void echo() {
        logger.info("message id : " + messageID);
        logger.info("properties : " + properties);
        logger.info("message : " + text);
        if (needReply) {
            messageSender.setCorrelationID(messageID);
            messageSender.send("★★★ " + text + " ★★★");
        }
    }

}
