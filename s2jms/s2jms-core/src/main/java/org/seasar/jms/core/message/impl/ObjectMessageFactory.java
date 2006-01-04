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

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * @author bowez
 */
@Component(instance = InstanceType.PROTOTYPE)
public class ObjectMessageFactory extends AbstractMessageFactory<ObjectMessage> {
    protected Serializable object;

    public ObjectMessageFactory() {
    }

    public ObjectMessageFactory(final Serializable object) {
        this.object = object;
    }

    public Serializable getObject() {
        return this.object;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setObject(final Serializable object) {
        this.object = object;
    }

    @Override
    protected ObjectMessage createMessageInstance(final Session session) throws JMSException {
        return session.createObjectMessage();
    }

    @Override
    protected void setupBody(final ObjectMessage message) throws JMSException {
        message.setObject(object);
    }
}
