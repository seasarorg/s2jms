/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.jms.core.message.impl;

import javax.jms.BytesMessage;
import javax.jms.Session;

import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class BytesMessageFactoryTest extends EasyMockTestCase {

    BytesMessageFactory target;

    Session session;

    BytesMessage message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new BytesMessageFactory();
        session = createStrictMock(Session.class);
        message = createStrictMock(BytesMessage.class);
    }

    /**
     * @throws Exception
     */
    public void testByteMessage() throws Exception {
        target.setCorrelationID("id");
        target.addProperty("foo", "FOO");
        target.setBytes(new byte[] { 1, 2, 3 });
        assertSame("1", message, target.createMessage(session));
    }

    /**
     * @throws Exception
     */
    public void recordByteMessage() throws Exception {
        expect(session.createBytesMessage()).andReturn(message);
        message.setJMSCorrelationID("id");
        message.setObjectProperty("foo", "FOO");
        message.writeBytes(aryEq(new byte[] { 1, 2, 3 }));
    }
}
