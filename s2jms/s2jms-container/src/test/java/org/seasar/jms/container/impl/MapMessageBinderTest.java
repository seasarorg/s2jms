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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.Session;
import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author Kenichiro Murata
 * 
 */
public class MapMessageBinderTest extends S2TestCase {

    private static final String PATH = "s2jms-container.dicon";

    private TransactionManager tm;
    private ConnectionFactory cf;

    private MapMessageBinder binder;
    private MapTest target;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(PATH);
        include("jms-activemq-outbound.dicon");

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

    public void testBindPeyLoad() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                MapMessage message = session.createMapMessage();
                message.setBoolean("invalid", false);
                message.setByte("id", (byte) 1);
                message.setBytes("relIds", new byte[] { 1, 2, 3, 4, 5 });
                message.setChar("head", (char) 1);
                message.setDouble("weight", 60.5d);
                message.setFloat("hight", 172.1f);
                message.setInt("serialNumber", 123456789);
                message.setLong("extendSerialNumber", 123456789L);
                message.setObject("obj", new Integer(1));
                message.setShort("extendId", (short) 1);
                message.setString("name", "Kenichiro Murata");
                message.setString("false", "Kenichiro Murata");

                MapTest srcTarget = new MapTest();
                BeanDesc beanDesc = BeanDescFactory.getBeanDesc(srcTarget.getClass());
                int propertySize = beanDesc.getPropertyDescSize();
                for (int idx = 0; idx < propertySize; idx++) {
                    PropertyDesc pd = beanDesc.getPropertyDesc(idx);
                    if (pd.getPropertyName().equals("false")) {
                        assertFalse(binder
                                .bindPayload(pd, srcTarget, pd.getPropertyName(), message));
                    } else {
                        assertTrue(binder.bindPayload(pd, srcTarget, pd.getPropertyName(), message));
                    }
                }

                assertEquals(srcTarget, target);

                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadBlank() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                MapMessage message = session.createMapMessage();

                MapTest srcTarget = new MapTest();
                BeanDesc beanDesc = BeanDescFactory.getBeanDesc(srcTarget.getClass());
                int propertySize = beanDesc.getPropertyDescSize();
                for (int idx = 0; idx < propertySize; idx++) {
                    PropertyDesc pd = beanDesc.getPropertyDesc(idx);
                    assertFalse(binder.bindPayload(pd, srcTarget, pd.getPropertyName(), message));
                }

                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] { MapMessageBinderTest.class.getName() });
    }

}
