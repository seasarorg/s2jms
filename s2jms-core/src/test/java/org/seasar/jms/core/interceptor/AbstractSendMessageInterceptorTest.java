/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.proxy.AopProxy;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.jms.core.impl.MessageSenderImpl;

/**
 * @author koichik
 */
public class AbstractSendMessageInterceptorTest extends TestCase {

    SendInterceptor interceptor;

    Hoge hoge;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        interceptor = new SendInterceptor();
        Aspect aspect = new AspectImpl(interceptor, new PointcutImpl(new String[] { ".*" }));
        hoge = (Hoge) new AopProxy(HogeImpl.class, new Aspect[] { aspect }).create();
    }

    /**
     * @throws Exception
     */
    public void testConcreteMethod() throws Exception {
        assertEquals("1", 100, hoge.foo());
    }

    /**
     * @throws Exception
     */
    public void testAbstractMethod() throws Exception {
        assertNull("1", hoge.bar());
    }

    /**
     * @throws Exception
     */
    public void testInitializeByType() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDef cd = new ComponentDefImpl(MessageSenderImpl.class);
        container.register(cd);

        interceptor.setContainer(container);
        interceptor.initialize();
        assertSame("1", cd, interceptor.componentDef);
    }

    /**
     * @throws Exception
     */
    public void testInitializeByName() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDef cd = new ComponentDefImpl(MessageSenderImpl.class, "sender");
        container.register(cd);

        interceptor.setContainer(container);
        interceptor.setMessageSenderName("sender");
        interceptor.initialize();
        assertSame("1", cd, interceptor.componentDef);
    }

    /**
     */
    public interface Hoge {

        /**
         * @return int
         */
        int foo();

        /**
         * @return String
         */
        String bar();
    }

    /**
     */
    public static abstract class HogeImpl implements Hoge {

        public int foo() {
            return 100;
        }
    }

    /**
     */
    public static class SendInterceptor extends AbstractSendMessageInterceptor {

        public Object invoke(MethodInvocation invocation) throws Throwable {
            return proceed(invocation);
        }
    }
}
