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
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author Kenichiro Murata
 * 
 */
public class TextMessageBinderTest extends S2TestCase {

    private static final String PATH = "s2jms-container.dicon";

    private TransactionManager tm;
    private ConnectionFactory cf;

    private TextMessageBinder binder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(PATH);
        include("jms-activemq-outbound.dicon");
    }

    public TextMessageBinderTest(String name) {
        super(name);
    }

    public void testGetPeyLoad() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                TextMessage message = session.createTextMessage("TEST");

                assertEquals("TEST", (String) binder.getPayload(message));
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
                TextMessage message = session.createTextMessage();

                assertNull(binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadNull() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                TextMessage message = session.createTextMessage(null);

                assertNull(binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] { TextMessageBinderTest.class.getName() });
    }

}
