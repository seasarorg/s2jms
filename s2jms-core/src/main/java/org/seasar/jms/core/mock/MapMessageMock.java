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
package org.seasar.jms.core.mock;

import java.util.Enumeration;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link javax.jms.MapMessage}のモックです。
 * 
 * @author koichik
 */
public class MapMessageMock extends MessageMock implements MapMessage {

    // instance fields
    /** JMSメッセージのペイロード */
    protected Map<String, Object> map = CollectionsUtil.newHashMap();

    /**
     * インスタンスを構築します。
     */
    public MapMessageMock() {
    }

    public boolean getBoolean(final String name) throws JMSException {
        return Boolean.class.cast(map.get(name));
    }

    public byte getByte(final String name) throws JMSException {
        return Byte.class.cast(map.get(name));
    }

    public byte[] getBytes(final String name) throws JMSException {
        return byte[].class.cast(map.get(name));
    }

    public char getChar(final String name) throws JMSException {
        return Character.class.cast(map.get(name));
    }

    public double getDouble(final String name) throws JMSException {
        return Double.class.cast(map.get(name));
    }

    public float getFloat(final String name) throws JMSException {
        return Float.class.cast(map.get(name));
    }

    public int getInt(final String name) throws JMSException {
        return Integer.class.cast(map.get(name));
    }

    public long getLong(final String name) throws JMSException {
        return Long.class.cast(map.get(name));
    }

    @SuppressWarnings("unchecked")
    public Enumeration getMapNames() throws JMSException {
        return new EnumerationAdapter(map.keySet().iterator());
    }

    public Object getObject(final String name) throws JMSException {
        return map.get(name);
    }

    public short getShort(final String name) throws JMSException {
        return Short.class.cast(map.get(name));
    }

    public String getString(final String name) throws JMSException {
        return String.class.cast(map.get(name));
    }

    public boolean itemExists(final String name) throws JMSException {
        return map.containsKey(name);
    }

    public void setBoolean(final String name, final boolean value) throws JMSException {
        map.put(name, value);
    }

    public void setByte(final String name, final byte value) throws JMSException {
        map.put(name, value);
    }

    public void setBytes(final String name, final byte[] value) throws JMSException {
        map.put(name, value);
    }

    public void setBytes(final String name, final byte[] value, final int offset, final int length) {
        final byte[] bytes = new byte[length];
        System.arraycopy(value, offset, bytes, 0, length);
        map.put(name, bytes);
    }

    public void setChar(final String name, final char value) throws JMSException {
        map.put(name, value);
    }

    public void setDouble(final String name, final double value) throws JMSException {
        map.put(name, value);
    }

    public void setFloat(final String name, final float value) throws JMSException {
        map.put(name, value);
    }

    public void setInt(final String name, final int value) throws JMSException {
        map.put(name, value);
    }

    public void setLong(final String name, final long value) throws JMSException {
        map.put(name, value);
    }

    public void setObject(final String name, final Object value) throws JMSException {
        map.put(name, value);
    }

    public void setShort(final String name, final short value) throws JMSException {
        map.put(name, value);
    }

    public void setString(final String name, final String value) throws JMSException {
        map.put(name, value);
    }

}
