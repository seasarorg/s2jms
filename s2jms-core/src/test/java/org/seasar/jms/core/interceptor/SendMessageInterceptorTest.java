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

import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.jms.core.MessageSender;

/**
 * @author koichik
 */
public class SendMessageInterceptorTest extends S2TigerTestCase {

    NormalTarget normal;

    FailTarget fail;

    MessageSender sender;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getSimpleName() + ".dicon");

        sender = createStrictMock(MessageSender.class);
        S2Container container = getContainer().getChild(0);
        container.register(sender, "sender");
        container.init();
    }

    /**
     * @throws Exception
     */
    public void testNormalReturn() throws Exception {
        assertEquals("1", "test", normal.execute());
    }

    /**
     * @throws Exception
     */
    public void recordNormalReturn() throws Exception {
        sender.send();
    }

    /**
     * @throws Exception
     */
    public void testThrownException() throws Exception {
        try {
            fail.execute();
            fail("1");
        } catch (RuntimeException expect) {
        }
    }

    interface Function {

        /**
         * @return String
         */
        String execute();

    }

    /**
     */
    public static class NormalTarget implements Function {

        public String execute() {
            return "test";
        }

    }

    /**
     */
    public static class FailTarget implements Function {

        public String execute() {
            throw new RuntimeException();
        }

    }

}
