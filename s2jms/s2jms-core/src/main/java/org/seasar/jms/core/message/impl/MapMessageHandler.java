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

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class MapMessageHandler extends AbstractMessageHandler<MapMessage, Map<String, Object>> {
    public MapMessageHandler() {
    }

    @Override
    public Map<String, Object> getBody() throws JMSException {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final String name : new IterableAdapter(message.getPropertyNames())) {
            map.put(name, getMessage().getObject(name));
        }
        return map;
    }

    public Class<MapMessage> getMessageType() {
        return MapMessage.class;
    }
}
