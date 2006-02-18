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

import javax.jms.BytesMessage;

import org.easymock.IArgumentMatcher;
import org.seasar.jca.unit.EasyMockTestCase;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.reportMatcher;

/**
 * @author Kenichiro Murata
 * 
 */
public class BytesMessageBinderTest extends EasyMockTestCase {

    private BytesMessageBinder binder;
    private BytesMessage message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        binder = new BytesMessageBinder();
        message = createStrictMock(BytesMessage.class);
    }

    public BytesMessageBinderTest(String name) {
        super(name);
    }

    public void testGetPayLoad() throws Exception {
        final byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertTrue(Arrays.equals(expected, (byte[]) binder.getPayload(message)));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getBodyLength()).andReturn((long) expected.length);
                expect(message.readBytes(eqBytes(expected))).andReturn(expected.length);
            }
        }.doTest();
    }

    public void testGetPayLoadNull() throws Exception {
        final byte[] expected = new byte[0];

        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertTrue(Arrays.equals(expected, (byte[]) binder.getPayload(message)));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getBodyLength()).andReturn((long) expected.length);
                expect(message.readBytes(eqBytes(expected))).andReturn(expected.length);
            }
        }.doTest();
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
