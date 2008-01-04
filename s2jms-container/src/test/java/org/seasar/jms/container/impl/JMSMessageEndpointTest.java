/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.jms.container.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.jms.Message;
import javax.jms.MessageListener;

import junit.framework.TestCase;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.jca.inbound.AbstractMessageEndpoint;
import org.seasar.jms.container.impl.JMSMessageEndpoint;

/**
 * @author koichik
 */
public class JMSMessageEndpointTest extends TestCase {

    private static Field beforeDeliveryCalled = ClassUtil.getDeclaredField(
            AbstractMessageEndpoint.class, "beforeDeliveryCalled");

    private static Field succeeded = ClassUtil.getDeclaredField(AbstractMessageEndpoint.class,
            "succeeded");

    private static Method doDelivery = ClassUtil.getDeclaredMethod(AbstractMessageEndpoint.class,
            "doDelivery", new Class[] { Object.class });
    static {
        beforeDeliveryCalled.setAccessible(true);
        succeeded.setAccessible(true);
        doDelivery.setAccessible(true);
    }

    /**
     * <p>
     * 「J2EE Connector Architecture Specification Version 1.5」の 「12.5.6
     * Transacted Delivery (Using Container-Managed Transaction)」 Option A<br>
     * beforeDelivery() および afterDelivery() が呼び出されないケースで，リスナーメソッドが正常終了する場合のテスト
     * </p>
     * 
     * @throws Exception
     */
    public void testTargetSuccessfullyCompletedA() throws Exception {
        JMSMessageEndpoint endpoint = new JMSMessageEndpoint(null, null, null, null,
                new MessageListener() {

                    public void onMessage(Message message) {
                    }
                });
        endpoint.onMessage(null);
        assertFalse(isBeforeDeliveryCalled(endpoint));
        assertFalse(isSucceeded(endpoint));
    }

    /**
     * <p>
     * 「J2EE Connector Architecture Specification Version 1.5」の 「12.5.6
     * Transacted Delivery (Using Container-Managed Transaction)」 Option A<br>
     * beforeDelivery() および afterDelivery() が呼び出されないケースで
     * リスナーメソッドが例外をスローする場合のテスト．
     * </p>
     * 
     * @throws Exception
     */
    public void testTargetFailedA() throws Exception {
        JMSMessageEndpoint endpoint = new JMSMessageEndpoint(null, null, null, null,
                new MessageListener() {

                    public void onMessage(Message message) {
                        throw new RuntimeException();
                    }
                });
        try {
            endpoint.onMessage(null);
            fail();
        } catch (RuntimeException expected) {
        }
        assertFalse(isBeforeDeliveryCalled(endpoint));
        assertFalse(isSucceeded(endpoint));
    }

    /**
     * <p>
     * 「J2EE Connector Architecture Specification Version 1.5」の 「12.5.6
     * Transacted Delivery (Using Container-Managed Transaction)」 Option B<br>
     * beforeDelivery() および afterDelivery() が呼び出されるケースでリスナーメソッドが正常終了する場合のテスト
     * </p>
     * 
     * @throws Exception
     */
    public void testTargetSuccessfullyCompletedB() throws Exception {
        JMSMessageEndpoint endpoint = new JMSMessageEndpoint(null, null, null, null,
                new MessageListener() {

                    public void onMessage(Message message) {
                    }
                });
        setBeforeDeliveryCalled(endpoint, true);
        endpoint.onMessage(null);
        assertTrue(isBeforeDeliveryCalled(endpoint));
        assertTrue(isSucceeded(endpoint));
    }

    /**
     * <p>
     * 「J2EE Connector Architecture Specification Version 1.5」の 「12.5.6
     * Transacted Delivery (Using Container-Managed Transaction)」 Option B<br>
     * beforeDelivery() および afterDelivery() が呼び出されるケースで
     * リスナーメソッドが例外をスローするする場合のテスト
     * </p>
     * <p>
     * 本来JCA仕様ではリスナーメソッドがスローした例外はリソースアダプタに伝播すべきであるが，
     * ActiveMQのリソースアダプタがメッセージ受信をやめてしまうため，例外を破棄することとする．
     * </p>
     * 
     * @throws Exception
     */
    public void testTargetFailedB() throws Exception {
        JMSMessageEndpoint endpoint = new JMSMessageEndpoint(null, null, null, null,
                new MessageListener() {

                    public void onMessage(Message message) {
                        throw new RuntimeException();
                    }
                });
        setBeforeDeliveryCalled(endpoint, true);
        try {
            endpoint.onMessage(null);
            fail();
        } catch (Exception expected) {
        }
        assertTrue(isBeforeDeliveryCalled(endpoint));
        assertFalse(isSucceeded(endpoint));
    }

    /**
     * @throws Exception
     */
    public void testClassLoader() throws Exception {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader loader = new URLClassLoader(new URL[0]);

        AbstractMessageEndpoint endpoint = new JMSMessageEndpoint(null, null, null, loader,
                new MessageListener() {

                    public void onMessage(Message message) {
                        assertSame("0", loader, Thread.currentThread().getContextClassLoader());
                    }
                });
        doDelivery(endpoint, null);
        assertSame(contextClassLoader, Thread.currentThread().getContextClassLoader());
    }

    /**
     * @throws Exception
     */
    public void testClassLoaderWithException() throws Exception {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader loader = new URLClassLoader(new URL[0]);

        AbstractMessageEndpoint endpoint = new JMSMessageEndpoint(null, null, null, loader,
                new MessageListener() {

                    public void onMessage(Message message) {
                        assertSame("0", loader, Thread.currentThread().getContextClassLoader());
                        throw new RuntimeException();
                    }
                });
        try {
            doDelivery(endpoint, null);
            fail();
        } catch (Exception expected) {
        }
        assertSame(contextClassLoader, Thread.currentThread().getContextClassLoader());
    }

    private boolean isBeforeDeliveryCalled(Object target) {
        return Boolean.class.cast(FieldUtil.get(beforeDeliveryCalled, target));
    }

    private void setBeforeDeliveryCalled(Object target, boolean value) {
        FieldUtil.set(beforeDeliveryCalled, target, value);
    }

    private boolean isSucceeded(Object target) {
        return Boolean.class.cast(FieldUtil.get(succeeded, target));
    }

    private Object doDelivery(Object target, Object arg) {
        return MethodUtil.invoke(doDelivery, target, new Object[] { arg });
    }
}
