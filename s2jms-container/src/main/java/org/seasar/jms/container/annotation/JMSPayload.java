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
package org.seasar.jms.container.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.framework.container.annotation.tiger.BindingType;

/**
 * リスナコンポーネントのフィールドまたはプロパティにJMSメッセージのペイロードをインジェクションすることを示します。
 * <p>
 * プロパティにインジェクションすることを示す場合は setter メソッドに注釈します。
 * </p>
 * 
 * @author y-komori
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.FIELD })
public @interface JMSPayload {

    /**
     * リスナコンポーネントにインジェクションするJMSペイロードの名前です。
     * <p>
     * {@link javax.jms.MapMessage}の場合はマッピングのキー名を指定することができます。 その他のメッセージ型の場合は
     * <code>payload</code> になります。 {@link javax.jms.MapMessage}で<code>payload</code>
     * を指定すると、 {@link javax.jms.MapMessage}の全てのマッピングを持つ{@link java.util.Map}がインジェクションされます．
     * </p>
     * <p>
     * 省略された場合は注釈されたフィールドまたはプロパティ名が使われます。
     * </p>
     * 
     * @return リスナコンポーネントにインジェクションするJMSメッセージのペイロードの名前
     */
    String name() default "";

    /**
     * バインディングタイプを指定します。
     * 
     * @return バインディングタイプ
     */
    BindingType bindingType() default BindingType.SHOULD;

}