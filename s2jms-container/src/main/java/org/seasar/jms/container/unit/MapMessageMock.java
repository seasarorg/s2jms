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
package org.seasar.jms.container.unit;

import java.util.Enumeration;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class MapMessageMock extends MessageMock implements MapMessage {

    protected Map<String, Object> map = CollectionsUtil.newHashMap();

    public MapMessageMock() {
    }

    public boolean getBoolean(String name) throws JMSException {
        return Boolean.class.cast(map.get(name));
    }

    public byte getByte(String name) throws JMSException {
        return Byte.class.cast(map.get(name));
    }

    public byte[] getBytes(String name) throws JMSException {
        return byte[].class.cast(map.get(name));
    }

    public char getChar(String name) throws JMSException {
        return Character.class.cast(map.get(name));
    }

    public double getDouble(String name) throws JMSException {
        return Double.class.cast(map.get(name));
    }

    public float getFloat(String name) throws JMSException {
        return Float.class.cast(map.get(name));
    }

    public int getInt(String name) throws JMSException {
        return Integer.class.cast(map.get(name));
    }

    public long getLong(String name) throws JMSException {
        return Long.class.cast(map.get(name));
    }

    public Enumeration getMapNames() throws JMSException {
        return new EnumerationAdapter(map.keySet().iterator());
    }

    public Object getObject(String name) throws JMSException {
        return map.get(name);
    }

    public short getShort(String name) throws JMSException {
        return Short.class.cast(map.get(name));
    }

    public String getString(String name) throws JMSException {
        return String.class.cast(map.get(name));
    }

    public boolean itemExists(String name) throws JMSException {
        return map.containsKey(name);
    }

    public void setBoolean(String name, boolean value) throws JMSException {
        map.put(name, value);
    }

    public void setByte(String name, byte value) throws JMSException {
        map.put(name, value);
    }

    public void setBytes(String name, byte[] value) throws JMSException {
        map.put(name, value);
    }

    public void setBytes(String name, byte[] value, int offset, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(value, offset, bytes, 0, length);
        map.put(name, bytes);
    }

    public void setChar(String name, char value) throws JMSException {
        map.put(name, value);
    }

    public void setDouble(String name, double value) throws JMSException {
        map.put(name, value);
    }

    public void setFloat(String name, float value) throws JMSException {
        map.put(name, value);
    }

    public void setInt(String name, int value) throws JMSException {
        map.put(name, value);
    }

    public void setLong(String name, long value) throws JMSException {
        map.put(name, value);
    }

    public void setObject(String name, Object value) throws JMSException {
        map.put(name, value);
    }

    public void setShort(String name, short value) throws JMSException {
        map.put(name, value);
    }

    public void setString(String name, String value) throws JMSException {
        map.put(name, value);
    }

}
