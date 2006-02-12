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
package org.seasar.jms.core.container.impl;

import java.lang.reflect.Method;

import javax.jms.JMSException;
import javax.jms.Message;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.jms.core.annotation.JMSPayload;

/**
 * @author y-komori
 * 
 */
public abstract class AnnotationMessageBinder<MSGTYPE extends Message> extends AbstractMessageBinder {
    @Override
    protected boolean bindPayload(PropertyDesc pd, Object target, String propertyName,
            Message message) throws JMSException {
        boolean hasBound = false;

        Method writeMethod = pd.getWriteMethod();
        if (writeMethod.isAnnotationPresent(JMSPayload.class)) {
            Object payload = getPayload(message);
            if (payload != null)
            {
                setValue(pd, target, payload);
                hasBound = true;
            }
        }

        return hasBound;
    }

    abstract protected Object getPayload(Message message) throws JMSException;
}
