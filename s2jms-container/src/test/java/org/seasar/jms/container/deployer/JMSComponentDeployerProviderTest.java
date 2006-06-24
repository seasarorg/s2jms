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

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.deployer.ComponentDeployerFactory.Provider;
import org.seasar.framework.container.impl.ComponentDefImpl;

/**
 * @author y-komori
 * 
 */
public class JMSComponentDeployerProviderTest extends S2TestCase {
    protected Provider provider;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        provider = new JMSComponentDeployerProvider();
    }

    public void testCreateRequestComponentDeployer() {
        ComponentDeployer deployer = provider
                .createRequestComponentDeployer(new ComponentDefImpl());
        assertNotNull("deployer is null", deployer);
        assertTrue("deployer isn't JMSRequestComponentDeployer.",
                deployer instanceof JMSRequestComponentDeployer);
    }
}
