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

import java.io.Serializable;
import java.util.Arrays;

import javax.jms.ObjectMessage;

import org.seasar.jca.unit.EasyMockTestCase;

import static org.easymock.EasyMock.expect;

/**
 * @author Kenichiro Murata
 * 
 */
public class ObjectMessageBinderTest extends EasyMockTestCase {

    private ObjectMessageBinder binder;
    private ObjectMessage message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        binder = new ObjectMessageBinder();
        message = createStrictMock(ObjectMessage.class);
    }

    public ObjectMessageBinderTest(String name) {
        super(name);
    }

    public void testGetPayloadNull() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertNull(binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(null);
            }
        }.doTest();
    }

    public void testGetPayloadArray() throws Exception {
        final int[] exptected = { 1, 2, 3, 4, 5 };
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertTrue(Arrays.equals(exptected, (int[]) binder.getPayload(message)));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(exptected);
            }
        }.doTest();
    }

    public void testGetPayloadBoolean() throws Exception {
        final boolean expected = true;
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public void testGetPayloadByte() throws Exception {
        final byte expected = 1;
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public void testGetPayloadFloat() throws Exception {
        final float expected = 1.0f;
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public void testGetPayloadDouble() throws Exception {
        final double expected = 1.0d;
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public void testGetPayloadInteger() throws Exception {
        final int expected = 1;
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public void testGetPayloadLong() throws Exception {
        final long expected = 1L;
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public void testGetPayloadShort() throws Exception {
        final short expected = 1;
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public void testGetPayloadString() throws Exception {
        final String expected = "TEST";
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public void testGetPayloadCustom() throws Exception {
        final ObjectTest expected = new ObjectTest();
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertEquals(expected, binder.getPayload(message));
                assertEquals("hello", ((ObjectTest) binder.getPayload(message)).getMessage());
            }

            @Override
            public void verify() throws Exception {
                expect(message.getObject()).andReturn(expected);
                expect(message.getObject()).andReturn(expected);
            }
        }.doTest();
    }

    public static class ObjectTest implements Serializable {

        private static final long serialVersionUID = 1L;

        private String message = "hello";

        public ObjectTest() {

        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object obj) {
            if (null != obj) {
                return obj.equals(this.message);
            } else if (null == this.message) {
                return true;
            }
            return super.equals(obj);
        }

    }
}
