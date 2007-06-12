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
package org.seasar.jms.core.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.annotation.tiger.Component;

/**
 * JMSメッセージを送信するインターセプタです。
 * <p>
 * ターゲットメソッドが例外をスローすることなく終了した場合、{@link org.seasar.jms.core.MessageSender#send() send()}メソッドを呼び出してJMSメッセージを送信します。
 * </p>
 * 
 * @author koichik
 */
@Component
public class SendMessageInterceptor extends AbstractSendMessageInterceptor {

    /**
     * ターゲットメソッドが例外をスローすることなく終了した後にJMSメッセージを送信します。
     * 
     * @param invocation
     *            ターゲットメソッドの呼び出しを表現するオブジェクト
     * @return ターゲットメソッドの戻り値
     * @throws Throwable
     *             ターゲットメソッドの実行時あるいはJMSメッセージの送信時に例外が発生した場合にスローされます
     */
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object result = proceed(invocation);
        getMessageSender().send();
        return result;
    }

}
