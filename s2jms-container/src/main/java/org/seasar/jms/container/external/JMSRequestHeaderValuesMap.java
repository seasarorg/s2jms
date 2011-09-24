/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import javax.jms.Message;

/**
 * JMSメッセージのヘッダおよびプロパティを外部コンテキストのリクエストマップとして扱うコンポーネントです。
 * 
 * @author koichik
 */
public class JMSRequestHeaderValuesMap extends JMSRequestHeaderMap {

    // constants
    /** 空の配列 */
    protected static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * インスタンスを構築します。
     * 
     * @param message
     */
    public JMSRequestHeaderValuesMap(final Message message) {
        super(message);
    }

    @Override
    protected Object getAttribute(final String key) {
        final Object value = super.getAttribute(key);
        return value == null ? EMPTY_ARRAY : new Object[] { value };
    }

}
