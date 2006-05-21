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

import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.tools.generic.RenderTool;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.exception.SRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.jms.core.text.TextProvider;

/**
 * Velocityを使ってフォーマットした文字列を提供する{@link org.seasar.jms.core.text.TextProvider}の実装クラス。
 * 
 * @author bowez
 */
@Component
public class VelocityTextFormatter implements TextProvider {
    private static final Logger logger = Logger.getLogger(VelocityTextFormatter.class);

    protected S2Container container;
    protected String templateText;
    protected RenderTool renderTool = new RenderTool();
    protected S2VelocityContext velocityContext = new S2VelocityContext();

    static {
        try {
            RuntimeSingleton.init();
        } catch (Exception e) {
            logger.log("EJMS0000", null, e);
        }
    }

    /**
     * インスタンスを構築します。
     * 
     */
    public VelocityTextFormatter() {
    }

    /**
     * テンプレート文字列を設定します(必須)。
     * 
     * @param templateText
     *            テンプレート文字列
     */
    @Binding(bindingType = BindingType.MUST)
    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }

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
     * レンダーツールを設定します。
     * 
     * @param renderTool
     *            レンダーツール
     */
    @Binding(bindingType = BindingType.MAY)
    public void setRenderTool(final RenderTool renderTool) {
        this.renderTool = renderTool;
    }

    /**
     * S2コンテナをコンテキストとしてテンプレート文字列を評価し、その結果の文字列を返します。
     * 
     * @return S2コンテナをコンテキストとしてテンプレート文字列を処理した結果の文字列
     */
    public String getText() {
        try {
            final String result = renderTool.eval(velocityContext, templateText);
            if (result != null) {
                return result;
            }
        } catch (final Exception e) {
            throw new SRuntimeException("EJMS0000", null, e);
        }
        throw new SRuntimeException("EJMS0000");
    }

    /**
     * S2コンテナをVelocityのコンテキストとして扱うためのクラスです。
     * 
     * @author koichik
     */
    class S2VelocityContext implements Context {
        /**
         * サポートされません。
         * 
         * @throws UnsupportedOperationException
         *             常にスローされます
         */
        public Object put(final String key, final Object value) {
            throw new UnsupportedOperationException();
        }

        /**
         * {@code key}をコンポーネント名としてS2コンテナから取得したコンポーネントを返します。
         * 
         * @param key
         *            コンポーネント名
         * @return コンポーネント名に対応付けられたコンポーネント
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
         * S2コンテナに{@code key}をコンポーネント名として持つコンポーネントが登録されていれば{@code true}を返します。
         * 
         * @param key
         *            コンポーネント名
         * @return S2コンテナに{@code key}をコンポーネント名として持つコンポーネントが登録されていれば{@code true}を、それ以外は{@code false}
         */
        public boolean containsKey(final Object key) {
            try {
                return container.hasComponentDef(key);
            } catch (final TooManyRegistrationRuntimeException e) {
                return false;
            }
        }

        /**
         * サポートされません。
         * 
         * @throws UnsupportedOperationException
         *             常にスローされます
         */
        public Object[] getKeys() {
            throw new UnsupportedOperationException();
        }

        /**
         * サポートされません。
         * 
         * @throws UnsupportedOperationException
         *             常にスローされます
         */
        public Object remove(final Object key) {
            throw new UnsupportedOperationException();
        }
    }
}
