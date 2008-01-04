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
package org.seasar.jms.core.text.impl;

import org.apache.velocity.context.Context;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

/**
 * S2コンテナをVelocityのコンテキストとして扱うためのクラスです。
 * 
 * @author bowez
 */
public class S2Context implements Context {

    // instance fields
    /** S2コンテナ */
    protected S2Container container;

    /**
     * S2コンテナを設定します(必須)。
     * 
     * @param container
     *            S2コンテナ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setContainer(final S2Container container) {
        this.container = container;
    }

    /**
     * サポートされません。常に例外がスローされます。
     * 
     * @throws UnsupportedOperationException
     */
    public Object put(final String key, final Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@code key}をコンポーネント名としてS2コンテナから取得したコンポーネントを返します。
     * 
     * @param key
     *            コンポーネント名
     * @return コンポーネント名に対応付けられたコンポーネント。コンポーネントが存在しない場合は null。
     */
    public Object get(final String key) {
        try {
            return container.getComponent(key);
        } catch (final ComponentNotFoundRuntimeException e) {
            return null;
        } catch (final TooManyRegistrationRuntimeException e) {
            return null;
        }
    }

    /**
     * {@code key}に対応するコンポーネントがコンテナに登録されているかどうかを調べます。
     * 
     * @param key
     *            コンポーネント名
     * @return コンポーネント名に対応づけられたコンポーネントが存在する場合は true
     */
    public boolean containsKey(final Object key) {
        try {
            return container.hasComponentDef(key);
        } catch (final ComponentNotFoundRuntimeException e) {
            return false;
        }
    }

    /**
     * サポートされません。常に例外がスローされます。
     * 
     * @throws UnsupportedOperationException
     */
    public Object[] getKeys() {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされません。常に例外がスローされます。
     * 
     * @throws UnsupportedOperationException
     */
    public Object remove(final Object key) {
        throw new UnsupportedOperationException();
    }

}
