/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.Arrays;

import javax.jms.BytesMessage;

import org.easymock.IArgumentMatcher;
import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class BytesMessageHandlerTest extends EasyMockTestCase {

    BytesMessageHandler target;

    BytesMessage message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target = new BytesMessageHandler();
        message = createStrictMock(BytesMessage.class);
    }

    /**
     * @throws Exception
     */
    public void testGetProperties() throws Exception {
        assertTrue("1", Arrays.equals(new byte[] { 1, 2, 3 }, target.handleMessage(message)));
    }

    /**
     * @throws Exception
     */
    public void recordGetProperties() throws Exception {
        expect(message.getBodyLength()).andReturn(3L);
        expect(message.readBytes(eqBytes(new byte[] { 1, 2, 3 }))).andReturn(3);
    }

    private static byte[] eqBytes(final byte[] expected) {
        reportMatcher(new IArgumentMatcher() {

            public boolean matches(Object arg) {
                byte[] actual = byte[].class.cast(arg);
                if (expected.length != actual.length) {
                    return false;
                }
                System.arraycopy(expected, 0, actual, 0, expected.length);
                return true;
            }

            public void appendTo(StringBuffer buf) {
                buf.append("eqBytes");
            }
        });
        return expected;
    }

}
