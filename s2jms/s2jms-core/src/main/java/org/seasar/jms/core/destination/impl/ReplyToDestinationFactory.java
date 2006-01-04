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
package org.seasar.jms.core.destination.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class ReplyToDestinationFactory extends AbstractDestinationFactory {
    protected Message message;

    public ReplyToDestinationFactory() {
    }

    public ReplyToDestinationFactory(final Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setMessage(final Message message) {
        this.message = message;
    }

    @Override
    protected Destination createDestination(final Session session) throws JMSException {
        return message.getJMSReplyTo();
    }
}
