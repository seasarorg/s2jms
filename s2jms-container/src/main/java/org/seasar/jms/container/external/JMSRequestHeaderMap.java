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
package org.seasar.jms.container.external;

import java.util.Iterator;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;

import org.seasar.framework.container.external.AbstractUnmodifiableExternalContextMap;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.util.IterableAdapter;
import org.seasar.jms.core.util.JMSHeaderSupport;

/**
 * JMSメッセージのヘッダおよびプロパティを外部コンテキストのリクエストマップとして扱うコンポーネントです。
 * 
 * @author koichik
 */
public class JMSRequestHeaderMap extends AbstractUnmodifiableExternalContextMap {

    // instance fields
    /** JMSメッセージ */
    protected final Message message;

    /** JMSメッセージが持つヘッダおよびプロパティの名前の{@link Set} */
    protected final Set<String> names = CollectionsUtil.newHashSet();

    /**
     * インスタンスを構築します。
     * 
     * @param message
     */
    public JMSRequestHeaderMap(final Message message) {
        this.message = message;
        try {
            names.addAll(JMSHeaderSupport.getNames());
            for (final String name : new IterableAdapter(message.getPropertyNames())) {
                names.add(name);
            }
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    @Override
    protected Object getAttribute(final String key) {
        try {
            final Object value = message.getObjectProperty(key);
            if (value != null) {
                return value;
            }
            return JMSHeaderSupport.getValue(message, key);
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator getAttributeNames() {
        return names.iterator();
    }

}
