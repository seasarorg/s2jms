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

import javax.jms.ObjectMessage;

import org.easymock.MockControl;
import org.seasar.jca.unit.EasyMockTestCase;

/**
 * @author Kenichiro Murata
 * 
 */
public class ObjectMessageBinderTest extends EasyMockTestCase {

    private ObjectMessageBinder binder;
    private ObjectMessage message;

    private MockControl messageControl;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        binder = new ObjectMessageBinder();
        messageControl = createStrictControl(ObjectMessage.class);
        message = (ObjectMessage) messageControl.getMock();
    }

    public ObjectMessageBinderTest(String name) {
        super(name);
    }

    public void testGetPayLoadNull() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                assertNull(binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                message.getObject();
                messageControl.setReturnValue(null);
            }
        }.doTest();
    }

    public void testGetPayLoadArray() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                int[] obj = { 1, 2, 3, 4, 5 };
                assertTrue(Arrays.equals(obj, (int[]) binder.getPayload(message)));
            }

            @Override
            public void verify() throws Exception {
                int[] obj = { 1, 2, 3, 4, 5 };
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadBoolean() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Boolean obj = new Boolean(true);
                assertEquals(obj, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                Boolean obj = new Boolean(true);
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadByte() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Byte obj = new Byte((byte) 1);
                assertEquals(obj, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                Byte obj = new Byte((byte) 1);
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadFloat() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Float obj = new Float(1.0f);
                assertEquals(obj, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                Float obj = new Float(1.0f);
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadDouble() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Double obj = new Double(1.0d);
                assertEquals(obj, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                Double obj = new Double(1.0d);
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadInteger() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Integer obj = new Integer(1);
                assertEquals(obj, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                Integer obj = new Integer(1);
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadLong() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Long obj = new Long(1);
                assertEquals(obj, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                Long obj = new Long(1);
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadShort() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                Short obj = new Short((short) 1);
                assertEquals(obj, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                Short obj = new Short((short) 1);
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadString() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                String obj = "TEST";
                assertEquals(obj, binder.getPayload(message));
            }

            @Override
            public void verify() throws Exception {
                String obj = "TEST";
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

    public void testGetPayLoadCustom() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                ObjectTest obj = new ObjectTest();
                assertEquals(obj, binder.getPayload(message));
                assertEquals("hello", ((ObjectTest) binder.getPayload(message)).getMessage());
            }

            @Override
            public void verify() throws Exception {
                ObjectTest obj = new ObjectTest();
                message.getObject();
                messageControl.setReturnValue(obj);
                message.getObject();
                messageControl.setReturnValue(obj);
            }
        }.doTest();
    }

}
