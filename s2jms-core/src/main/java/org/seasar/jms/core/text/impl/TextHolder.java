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
package org.seasar.jms.core.text.impl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.jms.core.text.TextProvider;

/**
 * インスタンスフィールドに保持した文字列を返す単純な{@link org.seasar.jms.core.text.TextProvider}の実装クラス。
 * 
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class TextHolder implements TextProvider {
    protected String text;

    /**
     * インスタンスを構築します。
     */
    public TextHolder() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param text
     *            文字列
     */
    public TextHolder(final String text) {
        this.text = text;
    }

    /**
     * 文字列を返します。
     * 
     * @return 文字列
     */
    public String getText() {
        return text;
    }

    /**
     * 文字列を設定します。
     * 
     * @param text
     *            文字列
     */
    @Binding(bindingType = BindingType.MAY)
    public void setText(final String text) {
        this.text = text;
    }
}
