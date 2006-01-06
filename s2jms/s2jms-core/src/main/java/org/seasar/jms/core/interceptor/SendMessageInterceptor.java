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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.util.MethodUtil;
import org.seasar.jms.core.impl.MessageSenderImpl;
import org.seasar.jms.core.message.MessageFactory;

/**
 * @author koichik
 */
@Component
public class SendMessageInterceptor extends MessageSenderImpl implements MethodInterceptor {
    protected S2Container container;
    protected String messageFactoryName;
    protected ComponentDef componentDef;

    public SendMessageInterceptor() {
    }

    public S2Container getContainer() {
        return container;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setContainer(final S2Container container) {
        this.container = container;
    }

    public String getMessageFactoryName() {
        return messageFactoryName;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setMessageFactoryName(final String messageFactoryName) {
        this.messageFactoryName = messageFactoryName;
    }

    @InitMethod
    public void initialize() {
        componentDef = container.getComponentDef(messageFactoryName == null ? MessageFactory.class
                : messageFactoryName);
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object result = proceed(invocation);
        send(getMessageFactory());
        return result;
    }

    protected Object proceed(final MethodInvocation invocation) throws Throwable {
        if (MethodUtil.isAbstract(invocation.getMethod())) {
            return null;
        }
        return invocation.proceed();
    }

    protected MessageFactory getMessageFactory() {
        return (MessageFactory) componentDef.getComponent();
    }
}
