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

package org.seasar.jms.container.filter.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.easymock.IArgumentMatcher;
import org.seasar.framework.unit.EasyMockTestCase;
import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.jms.container.filter.FilterChain;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.reportMatcher;

/**
 * @author koichik
 * 
 */
public class MessageDumpFilterTest extends EasyMockTestCase {

    protected DumpMessageFilter filter;
    protected FilterChain chain;
    protected TextMessage textMessage;
    protected BytesMessage bytesMessage;
    protected MapMessage mapMessage;
    protected ObjectMessage objectMessage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        filter = new DumpMessageFilter();
        chain = createStrictMock(FilterChain.class);
        textMessage = createStrictMock(TextMessage.class);
        bytesMessage = createStrictMock(BytesMessage.class);
        mapMessage = createStrictMock(MapMessage.class);
        objectMessage = createStrictMock(ObjectMessage.class);
    }

    public void testTextMessage() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                try {
                    filter.doFilter(textMessage, chain);
                } catch (Throwable e) {
                    fail();
                }
            }

            @Override
            public void record() throws Exception {
                expect(textMessage.getText()).andReturn("foo bar baz");
                try {
                    chain.doFilter(textMessage);
                } catch (Throwable e) {
                    fail();
                }
            }
        }.doTest();
    }

    public void testBytesMessage() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                try {
                    filter.doFilter(bytesMessage, chain);
                } catch (Throwable e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Override
            public void record() throws Exception {
                expect(bytesMessage.getBodyLength()).andReturn(20L);
                expect(bytesMessage.readBytes(eqArrayLength())).andReturn(16);
                expect(bytesMessage.readBytes(eqArrayLength())).andReturn(4);
                try {
                    chain.doFilter(bytesMessage);
                } catch (Throwable ignore) {
                }
            }
        }.doTest();
    }

    public void testMapMessage() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                try {
                    filter.doFilter(mapMessage, chain);
                } catch (Throwable e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Override
            public void record() throws Exception {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("a", "A");
                map.put("b", "B");
                expect(mapMessage.getMapNames()).andReturn(
                        new EnumerationAdapter(map.keySet().iterator()));
                expect(mapMessage.getObject("a")).andReturn("A");
                expect(mapMessage.getObject("b")).andReturn("B");
                try {
                    chain.doFilter(mapMessage);
                } catch (Throwable ignore) {
                }
            }
        }.doTest();
    }

    public void testObjectMessage() throws Exception {
        new Subsequence() {
            @Override
            public void replay() throws Exception {
                try {
                    filter.doFilter(objectMessage, chain);
                } catch (Throwable e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Override
            public void record() throws Exception {
                expect(objectMessage.getObject()).andReturn(new BigDecimal("100.00"));
                try {
                    chain.doFilter(objectMessage);
                } catch (Throwable ignore) {
                }
            }
        }.doTest();
    }

    public static byte[] eqArrayLength() {
        reportMatcher(new ArrayLengthMatcher());
        return null;
    }

    public static class ArrayLengthMatcher implements IArgumentMatcher {

        public ArrayLengthMatcher() {
        }

        public boolean matches(Object actual) {
            if (!byte[].class.isInstance(actual)) {
                return false;
            }
            byte[] bytes = byte[].class.cast(actual);
            if (bytes.length != 16) {
                return false;
            }
            for (int i = 0; i < 16; ++i) {
                bytes[i] = (byte) i;
            }
            return true;
        }

        public void appendTo(StringBuffer buf) {
            buf.append("eqArrayLength()");
        }
    }

}
