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
package org.seasar.jms.core;

import java.io.Serializable;
import java.util.Map;

import javax.jms.Message;

import org.seasar.jms.core.message.MessageFactory;

/**
 * @author koichik
 */
public interface MessageSender {
    void send();

    void send(byte[] bytes);

    void send(String text);

    void send(Serializable object);

    void send(Map<String, Object> map);

    <MSGTYPE extends Message> void send(MessageFactory<MSGTYPE> messageFactory);
}
