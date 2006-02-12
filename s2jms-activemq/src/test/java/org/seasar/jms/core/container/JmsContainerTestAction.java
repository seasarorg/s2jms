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
package org.seasar.jms.core.container;

import org.seasar.jms.core.annotation.JMSPayload;
import org.seasar.jms.core.annotation.MessageHandler;

/**
 * @author y-komori
 *
 */
public class JmsContainerTestAction {
    private int callCount;
    private String textPaylord;
    
    @MessageHandler
    public void caller()
    {
        callCount++;
    }
    
    public int getCallCount() {
        return callCount;
    }

    @JMSPayload
    public void setTextPaylord(String textPaylord) {
        this.textPaylord = textPaylord;
    }

    public String getTextPaylord() {
        return textPaylord;
    }
}
