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
package org.seasar.jms.core.interceptor;

import java.io.Serializable;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.util.MethodUtil;
import org.seasar.jms.core.MessageSender;
import org.seasar.jms.core.message.MessageFactory;

public class SendReturnValueInterceptor implements MethodInterceptor {
    protected S2Container container;
    protected String messageSenderName;
    protected ComponentDef componentDef;

    public SendReturnValueInterceptor() {
    }

    @Binding(bindingType = BindingType.MUST)
    public void setContainer(final S2Container container) {
        this.container = container;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setMessageSenderName(final String messageSenderName) {
        this.messageSenderName = messageSenderName;
    }

    @InitMethod
    public void initialize() {
        componentDef = container.getComponentDef(messageSenderName == null ? MessageFactory.class
                : messageSenderName);
    }

    @SuppressWarnings("unchecked")
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object result = proceed(invocation);
        if (result instanceof String) {
            getMessageSender().send(String.class.cast(result));
        } else if (result instanceof byte[]) {
            getMessageSender().send(byte[].class.cast(result));
        } else if (result instanceof Map) {
            getMessageSender().send((Map<String, Object>) result);
        } else if (result instanceof Serializable) {
            getMessageSender().send(Serializable.class.cast(result));
        } else {
            throw new IllegalStateException();
        }
        return result;
    }

    protected Object proceed(final MethodInvocation invocation) throws Throwable {
        if (MethodUtil.isAbstract(invocation.getMethod())) {
            return null;
        }
        return invocation.proceed();
    }

    protected MessageSender getMessageSender() {
        return (MessageSender) componentDef.getComponent();
    }
}
