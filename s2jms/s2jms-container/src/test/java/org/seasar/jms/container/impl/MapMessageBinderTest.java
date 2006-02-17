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
package org.seasar.jms.container.impl;

import javax.jms.MapMessage;

import org.easymock.MockControl;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jca.unit.EasyMockTestCase;

/**
 * @author Kenichiro Murata
 * 
 */
public class MapMessageBinderTest extends EasyMockTestCase {

    private MapMessageBinder binder;
    private MapMessage message;
    private MapTest target;

    private MockControl messageControl;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        binder = new MapMessageBinder();
        messageControl = createStrictControl(MapMessage.class);
        message = (MapMessage) messageControl.getMock();

        target = new MapTest();
        target.setExtendId((short) 1);
        target.setExtendSerialNumber(123456789L);
        target.setHead((char) 1);
        target.setHight(172.1f);
        target.setId((byte) 1);
        target.setInvalid(false);
        target.setName("Kenichiro Murata");
        target.setObj(new Integer(1));
        target.setRelIds(new byte[] { 1, 2, 3, 4, 5 });
        target.setSerialNumber(123456789);
        target.setWeight(60.5d);
    }

    public MapMessageBinderTest(String name) {
        super(name);
    }

    public void testBindPayLoad() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                MapTest srcTarget = new MapTest();
                BeanDesc beanDesc = BeanDescFactory.getBeanDesc(srcTarget.getClass());
                PropertyDesc pd = beanDesc.getPropertyDesc("invalid");
                assertTrue(binder.bindPayload(pd, srcTarget, "invalid", message));
                pd = beanDesc.getPropertyDesc("id");
                assertTrue(binder.bindPayload(pd, srcTarget, "id", message));
                pd = beanDesc.getPropertyDesc("relIds");
                assertTrue(binder.bindPayload(pd, srcTarget, "relIds", message));
                pd = beanDesc.getPropertyDesc("head");
                assertTrue(binder.bindPayload(pd, srcTarget, "head", message));
                pd = beanDesc.getPropertyDesc("weight");
                assertTrue(binder.bindPayload(pd, srcTarget, "weight", message));
                pd = beanDesc.getPropertyDesc("hight");
                assertTrue(binder.bindPayload(pd, srcTarget, "hight", message));
                pd = beanDesc.getPropertyDesc("serialNumber");
                assertTrue(binder.bindPayload(pd, srcTarget, "serialNumber", message));
                pd = beanDesc.getPropertyDesc("extendSerialNumber");
                assertTrue(binder.bindPayload(pd, srcTarget, "extendSerialNumber", message));
                pd = beanDesc.getPropertyDesc("obj");
                assertTrue(binder.bindPayload(pd, srcTarget, "obj", message));
                pd = beanDesc.getPropertyDesc("extendId");
                assertTrue(binder.bindPayload(pd, srcTarget, "extendId", message));
                pd = beanDesc.getPropertyDesc("name");
                assertTrue(binder.bindPayload(pd, srcTarget, "name", message));

                assertEquals(srcTarget, target);
            }

            @Override
            public void verify() throws Exception {
                message.itemExists("invalid");
                messageControl.setReturnValue(true);
                message.getObject("invalid");
                messageControl.setReturnValue((Object)false);
                message.itemExists("id");
                messageControl.setReturnValue(true);
                message.getObject("id");
                messageControl.setReturnValue((Object)(byte) 1);
                message.itemExists("relIds");
                messageControl.setReturnValue(true);
                message.getObject("relIds");
                messageControl.setReturnValue(new byte[] { 1, 2, 3, 4, 5 });
                message.itemExists("head");
                messageControl.setReturnValue(true);
                message.getObject("head");
                messageControl.setReturnValue((Object)(char) 1);
                message.itemExists("weight");
                messageControl.setReturnValue(true);
                message.getObject("weight");
                messageControl.setReturnValue((Object)60.5d);
                message.itemExists("hight");
                messageControl.setReturnValue(true);
                message.getObject("hight");
                messageControl.setReturnValue((Object)172.1f);
                message.itemExists("serialNumber");
                messageControl.setReturnValue(true);
                message.getObject("serialNumber");
                messageControl.setReturnValue((Object)123456789);
                message.itemExists("extendSerialNumber");
                messageControl.setReturnValue(true);
                message.getObject("extendSerialNumber");
                messageControl.setReturnValue((Object)123456789L);
                message.itemExists("obj");
                messageControl.setReturnValue(true);
                message.getObject("obj");
                messageControl.setReturnValue(new Integer(1));
                message.itemExists("extendId");
                messageControl.setReturnValue(true);
                message.getObject("extendId");
                messageControl.setReturnValue((Object)(short) 1);
                message.itemExists("name");
                messageControl.setReturnValue(true);
                message.getObject("name");
                messageControl.setReturnValue("Kenichiro Murata");
            }
        }.doTest();
    }

    public void testBindPayLoadNull() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                MapTest srcTarget = new MapTest();
                BeanDesc beanDesc = BeanDescFactory.getBeanDesc(srcTarget.getClass());
                PropertyDesc pd = beanDesc.getPropertyDesc("invalid");
                assertFalse(binder.bindPayload(pd, srcTarget, "invalid", message));
                pd = beanDesc.getPropertyDesc("id");
                assertFalse(binder.bindPayload(pd, srcTarget, "id", message));
                pd = beanDesc.getPropertyDesc("relIds");
                assertFalse(binder.bindPayload(pd, srcTarget, "relIds", message));
                pd = beanDesc.getPropertyDesc("head");
                assertFalse(binder.bindPayload(pd, srcTarget, "head", message));
                pd = beanDesc.getPropertyDesc("weight");
                assertFalse(binder.bindPayload(pd, srcTarget, "weight", message));
                pd = beanDesc.getPropertyDesc("hight");
                assertFalse(binder.bindPayload(pd, srcTarget, "hight", message));
                pd = beanDesc.getPropertyDesc("serialNumber");
                assertFalse(binder.bindPayload(pd, srcTarget, "serialNumber", message));
                pd = beanDesc.getPropertyDesc("extendSerialNumber");
                assertFalse(binder.bindPayload(pd, srcTarget, "extendSerialNumber", message));
                pd = beanDesc.getPropertyDesc("obj");
                assertFalse(binder.bindPayload(pd, srcTarget, "obj", message));
                pd = beanDesc.getPropertyDesc("extendId");
                assertFalse(binder.bindPayload(pd, srcTarget, "extendId", message));
                pd = beanDesc.getPropertyDesc("name");
                assertFalse(binder.bindPayload(pd, srcTarget, "name", message));

                assertEquals(srcTarget, new MapTest());
            }

            @Override
            public void verify() throws Exception {
                message.itemExists("invalid");
                messageControl.setReturnValue(false);
                message.itemExists("id");
                messageControl.setReturnValue(false);
                message.itemExists("relIds");
                messageControl.setReturnValue(false);
                message.itemExists("head");
                messageControl.setReturnValue(false);
                message.itemExists("weight");
                messageControl.setReturnValue(false);
                message.itemExists("hight");
                messageControl.setReturnValue(false);
                message.itemExists("serialNumber");
                messageControl.setReturnValue(false);
                message.itemExists("extendSerialNumber");
                messageControl.setReturnValue(false);
                message.itemExists("obj");
                messageControl.setReturnValue(false);
                message.itemExists("extendId");
                messageControl.setReturnValue(false);
                message.itemExists("name");
                messageControl.setReturnValue(false);
            }
        }.doTest();
    }

}
