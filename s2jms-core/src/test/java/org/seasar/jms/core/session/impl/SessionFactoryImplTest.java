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
package org.seasar.jms.core.session.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.seasar.framework.unit.EasyMockTestCase;
import org.seasar.jms.core.session.SessionHandler;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class SessionFactoryImplTest extends EasyMockTestCase {

    SessionFactoryImpl target;

    ConnectionFactory cf;

    Connection con;

    Session session;

    SessionHandler handler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new SessionFactoryImpl();
        cf = createStrictMock(ConnectionFactory.class);
        con = createStrictMock(Connection.class);
        session = createStrictMock(Session.class);
        handler = createStrictMock(SessionHandler.class);
    }

    /**
     * @throws Exception
     */
    public void testTransacted() throws Exception {
        target.setConnectionFactory(cf);
        target.operateSession(handler);
    }

    /**
     * @throws Exception
     */
    public void recordTransacted() throws Exception {
        expect(cf.createConnection()).andReturn(con);
        con.start();
        expect(con.createSession(true, Session.AUTO_ACKNOWLEDGE)).andReturn(session);
        handler.handleSession(session);
        session.close();
        con.close();
    }

    /**
     * @throws Exception
     */
    public void testNotTransacted() throws Exception {
        target.setConnectionFactory(cf);
        target.setTransacted(false);
        target.operateSession(handler);
    }

    /**
     * @throws Exception
     */
    public void recordNotTransacted() throws Exception {
        expect(cf.createConnection()).andReturn(con);
        con.start();
        expect(con.createSession(false, Session.AUTO_ACKNOWLEDGE)).andReturn(session);
        handler.handleSession(session);
        session.close();
        con.close();
    }

}
