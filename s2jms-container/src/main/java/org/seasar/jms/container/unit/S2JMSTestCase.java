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
package org.seasar.jms.container.unit;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.jms.container.impl.JMSExternalContext;
import org.seasar.jms.container.impl.JMSExternalContextComponentDefRegister;
import org.seasar.jms.container.impl.JMSRequestImpl;

/**
 * @author y-komori
 * 
 */
public abstract class S2JMSTestCase extends S2FrameworkTestCase {

    @Override
    protected void setUpContainer() throws Throwable {
        super.setUpContainer();

        setUpExternalContext(getContainer());
        ComponentDeployerFactory.setProvider(new ExternalComponentDeployerProvider());

    }

    protected void setUpExternalContext(final S2Container container) {
        ExternalContext externalContext = new JMSExternalContext();
        externalContext.setRequest(new JMSRequestImpl(new MapMessageMock()));

        container.setExternalContext(externalContext);
        container
                .setExternalContextComponentDefRegister(new JMSExternalContextComponentDefRegister());
    }
}
