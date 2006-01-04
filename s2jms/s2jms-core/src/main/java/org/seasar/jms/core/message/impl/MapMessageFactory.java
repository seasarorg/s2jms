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
package org.seasar.jms.core.message.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * @author bowez
 */
@Component(instance = InstanceType.PROTOTYPE)
public class MapMessageFactory extends AbstractMessageFactory<MapMessage> {
    protected Map<String, Object> map;

    public MapMessageFactory() {
    }

    public MapMessageFactory(final Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setMap(final Map<String, Object> map) {
        this.map = map;
    }

    public void addValue(final String key, final Object value) {
        if (this.map == null) {
            this.map = new HashMap<String, Object>();
        }
        this.map.put(key, value);
    }

    @Override
    protected MapMessage createMessageInstance(final Session session) throws JMSException {
        return session.createMapMessage();
    }

    @Override
    protected void setupBody(final MapMessage message) throws JMSException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final String name = entry.getKey();
            final Object value = entry.getValue();
            if (value instanceof Boolean) {
                message.setBoolean(name, (Boolean) value);
            } else if (value instanceof Byte) {
                message.setByte(name, (Byte) value);
            } else if (value instanceof Short) {
                message.setShort(name, (Short) value);
            } else if (value instanceof Character) {
                message.setChar(name, (Character) value);
            } else if (value instanceof Integer) {
                message.setInt(name, (Integer) value);
            } else if (value instanceof Long) {
                message.setLong(name, (Long) value);
            } else if (value instanceof Float) {
                message.setFloat(name, (Float) value);
            } else if (value instanceof Double) {
                message.setDouble(name, (Double) value);
            } else if (value instanceof String) {
                message.setString(name, (String) value);
            } else if (value instanceof byte[]) {
                message.setBytes(name, (byte[]) value);
            } else {
                message.setObject(name, value);
            }
        }
    }
}
