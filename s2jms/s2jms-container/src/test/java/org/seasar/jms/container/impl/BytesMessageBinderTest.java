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

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.seasar.jca.unit.EasyMockTestCase;

/**
 * @author Kenichiro Murata
 * 
 */
public class BytesMessageBinderTest extends EasyMockTestCase {

    private BytesMessageBinder binder;
    private BytesMessage message;

    private MockControl messageControl;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        binder = new BytesMessageBinder();
        messageControl = createStrictControl(BytesMessage.class);
        message = (BytesMessage) messageControl.getMock();
    }

    public BytesMessageBinderTest(String name) {
        super(name);
    }

    public void testGetPayLoad() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                byte[] obj = new byte[] { 1, 2, 3, 4, 5 };
                assertTrue(Arrays.equals(obj, (byte[]) binder.getPayload(message)));
            }

            @Override
            public void verify() throws Exception {
                byte[] obj = new byte[] { 1, 2, 3, 4, 5 };
                message.getBodyLength();
                messageControl.setReturnValue(obj.length);
                message.readBytes(obj);
                messageControl.setMatcher(new ArgumentsMatcher() {
                    public boolean matches(Object[] expected, Object[] actual) {
                        byte[] expectedBytes = (byte[]) expected[0];
                        byte[] actualBytes = (byte[]) actual[0];

                        if (expectedBytes.length != actualBytes.length) {
                            return false;
                        }
                        System.arraycopy(expectedBytes, 0, actualBytes, 0, expectedBytes.length);
                        return true;
                    }

                    public String toString(Object[] argument) {
                        return super.toString();
                    }
                });
                messageControl.setReturnValue(obj.length);
            }
        }.doTest();
    }

    public void testGetPayLoadNull() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertTrue(Arrays.equals(new byte[0], (byte[]) binder.getPayload(message)));
            }

            @Override
            public void verify() throws Exception {
                byte[] obj = new byte[0];
                message.getBodyLength();
                messageControl.setReturnValue(obj.length);
                message.readBytes(obj);
                messageControl.setMatcher(new ArgumentsMatcher() {
                    public boolean matches(Object[] expected, Object[] actual) {
                        byte[] expectedBytes = (byte[]) expected[0];
                        byte[] actualBytes = (byte[]) actual[0];

                        if (expectedBytes.length != actualBytes.length) {
                            return false;
                        }
                        System.arraycopy(expectedBytes, 0, actualBytes, 0, expectedBytes.length);
                        return true;
                    }

                    public String toString(Object[] argument) {
                        return super.toString();
                    }
                });
                messageControl.setReturnValue(obj.length);
            }
        }.doTest();
    }
}
