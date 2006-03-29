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

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.hotswap.Hotswap;
import org.seasar.jms.container.JMSRequest;
import org.seasar.jms.container.impl.JMSExternalContext;
import org.seasar.jms.container.impl.JMSRequestImpl;

/**
 * @author y-komori
 * 
 */
public class JMSRequestComponentDeployerTest extends TestCase {
    public void testDeployAutoAutoConstructor() throws Exception {
        ExternalContext externalContext = new JMSExternalContext();
        JMSRequest request = new JMSRequestImpl();
        externalContext.setRequest(request);
        S2Container container = new S2ContainerImpl();
        container.setExternalContext(externalContext);

        ComponentDef cd = new ComponentDefImpl(Foo.class, "foo");
        container.register(cd);

        ComponentDeployer deployer = new JMSRequestComponentDeployer(cd);
        Foo foo = (Foo) deployer.deploy();

        assertSame("1", foo, request.getAttribute("foo"));
        assertSame("2", foo, deployer.deploy());
        
        container.getExternalContext().setRequest(null);
        try{
            deployer.deploy();
            fail("3");
        }
        catch (EmptyRuntimeException ex)
        {
        }
    }

    public void testDeployForHotswap() throws Exception {
        ExternalContext externalContext = new JMSExternalContext();
        JMSRequest request = new JMSRequestImpl();
        externalContext.setRequest(request);
        S2Container container = new S2ContainerImpl();
        container.setHotswapMode(true);
        container.setExternalContext(externalContext);

        ComponentDef cd = new ComponentDefImpl(Foo.class, "foo");
        container.register(cd);
        container.init();

        ComponentDeployer deployer = new JMSRequestComponentDeployer(cd);
        Foo foo = (Foo) deployer.deploy();

        Hotswap hotswap = cd.getHotswap();
        Thread.sleep(500);
        hotswap.getFile().setLastModified(new Date().getTime());
        assertNotSame("1", foo.getClass(), deployer.deploy().getClass());
    }

    public static class Foo {

        private String message;

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
