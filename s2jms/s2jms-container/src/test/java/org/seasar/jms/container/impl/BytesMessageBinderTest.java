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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author Kenichiro Murata
 * 
 */
public class BytesMessageBinderTest extends S2TestCase {

    private static final String PATH = "s2jms-container.dicon";

    private TransactionManager tm;
    private ConnectionFactory cf;

    private BytesMessageBinder binder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(PATH);
        include("jms-activemq-outbound.dicon");
    }

    public BytesMessageBinderTest(String name) {
        super(name);
    }

    public void testGetPeyLoad() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                BytesMessage message = session.createBytesMessage();
                message.writeObject(new Integer(1));
                message.reset();

                FileOutputStream fos = new FileOutputStream("t.tmp");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(new Integer(1));
                FileInputStream fis = new FileInputStream("t.tmp");
                ObjectInputStream ois = new ObjectInputStream(fis);
                byte[] except = new byte[0];
                ois.read(except);

                assertTrue(Arrays.equals(except, (byte[]) binder.getPayload(message)));

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
                BytesMessage message = session.createBytesMessage();
                message.reset();

                assertTrue(Arrays.equals(new byte[0], (byte[]) binder.getPayload(message)));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] { BytesMessageBinderTest.class.getName() });
    }

}
