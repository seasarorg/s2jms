/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class DumpMessageFilterTest extends EasyMockTestCase {

    DumpMessageFilter filter;

    FilterChain chain;

    TextMessage textMessage;

    BytesMessage bytesMessage;

    MapMessage mapMessage;

    ObjectMessage objectMessage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        filter = new DumpMessageFilter();
        filter.setForceDump(true);
        filter.setDumpHeader(false);
        filter.setDumpProperty(false);
        chain = createStrictMock(FilterChain.class);
        textMessage = createStrictMock(TextMessage.class);
        bytesMessage = createStrictMock(BytesMessage.class);
        mapMessage = createStrictMock(MapMessage.class);
        objectMessage = createStrictMock(ObjectMessage.class);
    }

    /**
     * @throws Exception
     */
    public void testTextMessage() throws Exception {
        try {
            System.out.println("before call doFilter");
            filter.doFilter(textMessage, chain);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws Exception
     */
    public void recordTextMessage() throws Exception {
        expect(textMessage.getText()).andReturn("foo bar baz");
        try {
            chain.doFilter(textMessage);
        } catch (Throwable ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testBytesMessage() throws Exception {
        try {
            filter.doFilter(bytesMessage, chain);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws Exception
     */
    public void recordBytesMessage() throws Exception {
        expect(bytesMessage.getBodyLength()).andReturn(20L);
        expect(bytesMessage.readBytes(eqArrayLength())).andReturn(16);
        expect(bytesMessage.readBytes(eqArrayLength())).andReturn(4);
        try {
            chain.doFilter(bytesMessage);
        } catch (Throwable ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testMapMessage() throws Exception {
        try {
            filter.doFilter(mapMessage, chain);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws Exception
     */
    public void recordMapMessage() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("a", "A");
        map.put("b", "B");
        expect(mapMessage.getMapNames()).andReturn(new EnumerationAdapter(map.keySet().iterator()));
        expect(mapMessage.getObject("a")).andReturn("A");
        expect(mapMessage.getObject("b")).andReturn("B");
        try {
            chain.doFilter(mapMessage);
        } catch (Throwable ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testObjectMessage() throws Exception {
        try {
            filter.doFilter(objectMessage, chain);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws Exception
     */
    public void recordObjectMessage() throws Exception {
        expect(objectMessage.getObject()).andReturn(new BigDecimal("100.00"));
        try {
            chain.doFilter(objectMessage);
        } catch (Throwable ignore) {
        }
    }

    /**
     * @return <code>null</code>を返します
     */
    public static byte[] eqArrayLength() {
        reportMatcher(new ArrayLengthMatcher());
        return null;
    }

    /**
     * @author koichik
     * 
     */
    public static class ArrayLengthMatcher implements IArgumentMatcher {

        /**
         * 
         */
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
                bytes[i] = (byte) (i + 'a');
            }
            return true;
        }

        public void appendTo(StringBuffer buf) {
            buf.append("eqArrayLength()");
        }

    }

}
