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
package org.seasar.jms.container.exception;

import java.lang.reflect.Method;

import org.seasar.framework.exception.SRuntimeException;

/**
 * メッセージリスナーコンポーネントのリスナーメソッドが不正な場合にスローされます。
 * 
 * @author koichik
 */
public class IllegalMessageListenerException extends SRuntimeException {

    // static fields
    private static final long serialVersionUID = 1L;

    /**
     * インスタンスを構築します。
     * 
     * @param method
     *            不正なリスナーメソッド
     */
    public IllegalMessageListenerException(final Method method) {
        super("EJMS-CONTAINER2001", new Object[] { method });
    }

}
