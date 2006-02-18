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

import java.util.Arrays;

import javax.jms.MapMessage;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jca.unit.EasyMockTestCase;

import static org.easymock.EasyMock.expect;

/**
 * @author Kenichiro Murata
 * 
 */
public class MapMessageBinderTest extends EasyMockTestCase {

    private MapMessageBinder binder;
    private MapMessage message;
    private MapTest target;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        binder = new MapMessageBinder();
        message = createStrictMock(MapMessage.class);

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

    public void testBindPayload() throws Exception {
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
                expect(message.itemExists("invalid")).andReturn(true);
                expect(message.getObject("invalid")).andReturn(false);
                expect(message.itemExists("id")).andReturn(true);
                expect(message.getObject("id")).andReturn(new Byte((byte) 1));
                expect(message.itemExists("relIds")).andReturn(true);
                expect(message.getObject("relIds")).andReturn(new byte[] { 1, 2, 3, 4, 5 });
                expect(message.itemExists("head")).andReturn(true);
                expect(message.getObject("head")).andReturn((char) 1);
                expect(message.itemExists("weight")).andReturn(true);
                expect(message.getObject("weight")).andReturn(60.5d);
                expect(message.itemExists("hight")).andReturn(true);
                expect(message.getObject("hight")).andReturn(172.1f);
                expect(message.itemExists("serialNumber")).andReturn(true);
                expect(message.getObject("serialNumber")).andReturn(123456789);
                expect(message.itemExists("extendSerialNumber")).andReturn(true);
                expect(message.getObject("extendSerialNumber")).andReturn(123456789L);
                expect(message.itemExists("obj")).andReturn(true);
                expect(message.getObject("obj")).andReturn(new Integer(1));
                expect(message.itemExists("extendId")).andReturn(true);
                expect(message.getObject("extendId")).andReturn(1);
                expect(message.itemExists("name")).andReturn(true);
                expect(message.getObject("name")).andReturn("Kenichiro Murata");
            }
        }.doTest();
    }

    public void testBindPayloadNull() throws Exception {
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
                expect(message.itemExists("invalid")).andReturn(false);
                expect(message.itemExists("id")).andReturn(false);
                expect(message.itemExists("relIds")).andReturn(false);
                expect(message.itemExists("head")).andReturn(false);
                expect(message.itemExists("weight")).andReturn(false);
                expect(message.itemExists("hight")).andReturn(false);
                expect(message.itemExists("serialNumber")).andReturn(false);
                expect(message.itemExists("extendSerialNumber")).andReturn(false);
                expect(message.itemExists("obj")).andReturn(false);
                expect(message.itemExists("extendId")).andReturn(false);
                expect(message.itemExists("name")).andReturn(false);
            }
        }.doTest();
    }

    public static class MapTest {

        private boolean invalid;
        private byte id;
        private byte[] relIds;
        private char head;
        private double weight;
        private float hight;
        private int serialNumber;
        private long extendSerialNumber;
        private short extendId;
        private String name;
        private Object obj;

        public short getExtendId() {
            return extendId;
        }

        public void setExtendId(short extendId) {
            this.extendId = extendId;
        }

        public long getExtendSerialNumber() {
            return extendSerialNumber;
        }

        public void setExtendSerialNumber(long extendSerialNumber) {
            this.extendSerialNumber = extendSerialNumber;
        }

        public char getHead() {
            return head;
        }

        public void setHead(char head) {
            this.head = head;
        }

        public float getHight() {
            return hight;
        }

        public void setHight(float hight) {
            this.hight = hight;
        }

        public byte getId() {
            return id;
        }

        public void setId(byte id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public int getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(int serialNumber) {
            this.serialNumber = serialNumber;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public boolean isInvalid() {
            return invalid;
        }

        public void setInvalid(boolean invalid) {
            this.invalid = invalid;
        }

        public byte[] getRelIds() {
            return relIds;
        }

        public void setRelIds(byte[] relIds) {
            this.relIds = relIds;
        }

        @Override
        public boolean equals(Object obj) {
            if (null != obj) {
                MapTest tmp = (MapTest) obj;
                return (this.extendId == tmp.getExtendId()
                        && this.extendSerialNumber == tmp.getExtendSerialNumber()
                        && this.head == tmp.getHead()
                        && this.hight == tmp.getHight()
                        && this.id == tmp.getId()
                        && ((this.name != null && this.name.equals(tmp.getName())) || (this.name == null && tmp
                                .getName() == null))
                        && this.invalid == tmp.isInvalid()
                        && ((this.obj != null && this.obj.equals(tmp.getObj())) || (this.obj == null && tmp
                                .getObj() == null)) && Arrays.equals(this.relIds, tmp.getRelIds())
                        && this.serialNumber == tmp.getSerialNumber() && this.weight == tmp
                        .getWeight());
            }
            return super.equals(obj);
        }
    }
}
