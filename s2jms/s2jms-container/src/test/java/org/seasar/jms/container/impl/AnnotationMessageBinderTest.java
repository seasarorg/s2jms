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

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jca.unit.S2EasyMockTestCase;
import org.seasar.jms.container.annotation.JMSPayload;

/**
 * @author y-komori
 * 
 */
public class AnnotationMessageBinderTest extends S2EasyMockTestCase {
    protected TextMessageBinder binder;
    protected BindTarget target;
    protected TextMessage message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(this.getClass().getSimpleName() + ".dicon");

        binder = new TextMessageBinder();
        message = createNiceMock(TextMessage.class);
    }

    public void testBindPayload() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(target.getClass());
        PropertyDesc pd = beanDesc.getPropertyDesc("text");
        assertTrue(binder.bindPayload(pd, target, "text", message));
        
        assertEquals("TextPayload", target.getText());
    }
    
    public static class BindTarget{
        private String text;

        public String getText() {
            return text;
        }
        
        @JMSPayload
        public void setText(String text) {
            this.text = text;
        }
    }
    
    public static class TextMessageBinder extends AnnotationMessageBinder<TextMessage> {
        @Override
        protected Object getPayload(TextMessage message) throws JMSException {
            return "TextPayload";
        }

        public Class<TextMessage> getMessageType() {
            return TextMessage.class;
        }
    }
}
