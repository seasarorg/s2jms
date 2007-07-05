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
package org.seasar.jms.container.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JMSメッセージを受信した際に、JMSコンテナから呼び出されるリスナーメソッドを指定するためのアノテーションです。
 * <p>
 * このアノテーションを付加されるメソッドは、引数の数が0または1でなくてはなりません。 引数が1つで、その型が{@link javax.jms.Message}の場合は、受信したJMSメッセージを引数として呼び出されます。
 * 引数が1つで、その型が{@link javax.jms.Message}以外の場合は、受信したJMSメッセージのペイロードを引数として呼び出されます。
 * </p>
 * 
 * @author y-komori
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnMessage {
}
