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
package org.seasar.jms.core.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * JMSで発生したエラーを通知するための実行時例外。
 * 
 * @author koichik
 */
public class SJMSRuntimeException extends SRuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     */
    public SJMSRuntimeException(final String messageCode) {
        this(messageCode, null, null);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            メッセージに埋め込まれる引数
     */
    public SJMSRuntimeException(final String messageCode, final Object[] args) {
        this(messageCode, args, null);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param cause
     *            この例外の原因となった例外
     */
    public SJMSRuntimeException(final String messageCode, final Throwable cause) {
        this(messageCode, null, cause);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            メッセージに埋め込まれる引数
     * @param cause
     *            この例外の原因となった例外
     */
    public SJMSRuntimeException(final String messageCode, final Object[] args, final Throwable cause) {
        super(messageCode, args, cause);
    }
}