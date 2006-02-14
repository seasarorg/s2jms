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
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author Kenichiro Murata
 * 
 */
public class ObjectMessageBinderTest extends S2TestCase {

    private static final String PATH = "s2jms-container.dicon";

    private TransactionManager tm;
    private ConnectionFactory cf;

    private ObjectMessageBinder binder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(PATH);
        include("jms-activemq-outbound.dicon");
    }

    public ObjectMessageBinderTest(String name) {
        super(name);
    }

    public void testGetPeyLoadBlank() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                ObjectMessage message = session.createObjectMessage();

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
                ObjectMessage message = session.createObjectMessage(null);

                assertNull(binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadArray() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                int[] obj = { 1, 2, 3, 4, 5 };
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadBoolean() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                Boolean obj = new Boolean(true);
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadByte() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                Byte obj = new Byte((byte) 1);
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadFloat() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                Float obj = new Float(1.0f);
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadDouble() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                Double obj = new Double(1.0);
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadInteger() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                Integer obj = new Integer(1);
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadLong() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                Long obj = new Long(1);
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadShort() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                Short obj = new Short((short) 1);
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadString() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                String obj = "TEST";
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public void testGetPeyLoadCustom() throws Exception {
        tm.begin();
        try {
            Connection con = cf.createConnection();
            try {
                Session session = con.createSession(true, Session.SESSION_TRANSACTED);
                ObjectTest obj = new ObjectTest();
                ObjectMessage message = session.createObjectMessage(obj);

                assertEquals(obj, binder.getPayload(message));
                assertEquals("hello", ((ObjectTest) binder.getPayload(message)).getMessage());
                session.close();
            } finally {
                con.close();
            }
        } finally {
            tm.commit();
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] { ObjectMessageBinderTest.class.getName() });
    }

}
