/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.jms.core.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link java.util.Enumeration}を{@link java.lang.Iterable}に扱うためのユーティリティ。
 * 
 * @author koichik
 */
@SuppressWarnings("unchecked")
public class IterableAdapter implements Iterable<String>, Iterator<String> {

    // instance fields
    /** 列挙 */
    Enumeration enumeration;

    /**
     * インスタンスを構築します。
     * 
     * @param enumeration
     *            反復対象となる{@link java.util.Enumeration}
     */
    public IterableAdapter(final Enumeration enumeration) {
        this.enumeration = enumeration;
    }

    /**
     * {@link java.util.Enumeration}の反復子を返します。
     * 
     * @return {@link java.util.Enumeration}の反復子
     */
    public Iterator<String> iterator() {
        return this;
    }

    /**
     * @see java.util.Iterator#hasNext
     */
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    /**
     * @see java.util.Iterator#hasNext
     */
    public String next() {
        return String.class.cast(enumeration.nextElement());
    }

    /**
     * このメソッドはサポートされません。
     * 
     * @see java.util.Iterator#remove
     */
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

}
