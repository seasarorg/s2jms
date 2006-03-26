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
package org.seasar.jms.container.deployer;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.deployer.AbstractComponentDeployer;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.hotswap.Hotswap;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.JMSRequest;
import org.seasar.jms.container.impl.JMSExternalContext;

/**
 * @author y-komori
 * 
 */
public class JMSRequestComponentDeployer extends AbstractComponentDeployer {
    private static Logger logger = Logger.getLogger(JMSRequestComponentDeployer.class);

    public JMSRequestComponentDeployer(ComponentDef componentDef) {
        super(componentDef);
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#deploy()
     */
    public Object deploy() {
        ComponentDef cd = getComponentDef();
        JMSRequest request = null;
        ExternalContext extCtx = cd.getContainer().getRoot().getExternalContext();
        if (extCtx != null && extCtx.getRequest() instanceof JMSExternalContext) {
            request = JMSRequest.class.cast(extCtx.getRequest());
        }
        if (request == null) {
            RuntimeException re = new EmptyRuntimeException("request");
            logger.log(re);
            throw re;
        }
        String componentName = getComponentName();
        Object component = null;
        Hotswap hotswap = cd.getHotswap();
        if (hotswap == null || !hotswap.isModified()) {
            component = request.getAttribute(componentName);
            if (component != null) {
                return component;
            }
        }
        component = getConstructorAssembler().assemble();
        request.setAttribute(componentName, component);
        getPropertyAssembler().assemble(component);
        getInitMethodAssembler().assemble(component);
        return component;
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#injectDependency(java.lang.Object)
     */
    public void injectDependency(Object outerComponent) {
        throw new UnsupportedOperationException("injectDependency");
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#init()
     */
    public void init() {
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#destroy()
     */
    public void destroy() {
    }
}
