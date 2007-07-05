/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.log.Logger;

/**
 * Velocityを使ってフォーマットした文字列を提供する{@link org.seasar.jms.core.text.TextProvider}の実装クラス。
 * 
 * @author bowez
 */
@Component
public class VelocityTextFormatter extends AbstractVelocityFormatter {

    // static fields
    private static final Logger logger = Logger.getLogger(VelocityTextFormatter.class);

    /** テンプレート文字列 */
    protected String templateText;

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
    public void setTemplateText(final String templateText) {
        this.templateText = templateText;
    }

    @Override
    protected synchronized void eval(final StringWriter out) throws Exception {
        boolean succeeded = velocityEngine.evaluate(context, out, "", templateText);
        if (!succeeded) {
            logger.error("Failed to evaluate velocity template text: " + templateText);
        }
    }

}
