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

import java.io.StringWriter;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.exception.SRuntimeException;
import org.seasar.jms.core.text.TextProvider;

/**
 * Velocityを使ってフォーマットした文字列を提供する{@link org.seasar.jms.core.text.TextProvider}の
 * 抽象クラスです。
 * 
 * @author bowez
 */
public abstract class AbstractVelocityFormatter implements TextProvider {
    protected VelocityEngine velocityEngine = new VelocityEngine();
    protected Context context = new S2Context();

    public AbstractVelocityFormatter() {
        try {
            velocityEngine.init();
        } 
        catch (final Exception e) {
            throw new SRuntimeException("EJMS0000", null, e);
        }
    }
    
    @Binding(bindingType = BindingType.MAY)
    public void setContext(final Context context) {
        this.context = context;
    }

    /**
     * S2コンテナをコンテキストとしてテンプレート文字列を評価し、その結果の文字列を返します。
     * 
     * @return S2コンテナをコンテキストとしてテンプレート文字列を処理した結果の文字列
     */
    public String getText() {
        final StringWriter out = new StringWriter();
        try {
            eval(out);
        } 
        catch (final Exception e) {
            throw new SRuntimeException("EJMS0000", null, e);
        }
        return out.toString();
    }
    
    protected abstract void eval(StringWriter out) throws Exception;
}
