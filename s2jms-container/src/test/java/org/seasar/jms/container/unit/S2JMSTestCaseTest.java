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

import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.ExternalContext;
import org.seasar.jms.container.JMSRequest;
import org.seasar.jms.container.external.JMSExternalContext;
import org.seasar.jms.core.mock.MapMessageMock;

/**
 * @author y-komori
 */
public class S2JMSTestCaseTest extends S2JMSTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registerMessage(new MapMessageMock());
    }

    /**
     * 
     */
    public void testSetUpContainer() {
        ExternalContext externalContext = getContainer().getExternalContext();
        assertEquals(JMSExternalContext.class, externalContext.getClass());

        Object request = externalContext.getRequest();
        assertNotNull("externalContext.getRequest() is null.", request);
        assertTrue("externalContext.getRequest() isn't JMSRequest", request instanceof JMSRequest);

        request = getContainer().getComponent(ContainerConstants.REQUEST_NAME);
        assertNotNull("container.getComponent(\"request\") is null.", request);
        assertTrue("container.getComponent(\"request\") isn't JMSRequest.",
                request instanceof JMSRequest);
    }

}
