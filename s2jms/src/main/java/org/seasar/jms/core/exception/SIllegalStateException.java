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

import javax.jms.IllegalStateException;

import org.seasar.framework.message.MessageFormatter;

/**
 * リソースバンドルに外部化されたメッセージをコードで指定することのできる{@link javax.jms.IllegalStateException}の拡張クラス。
 * 
 * @author koichik
 */
public class SIllegalStateException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    protected String messageCode;
    protected Object[] args;

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     */
    public SIllegalStateException(final String messageCode) {
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
    public SIllegalStateException(final String messageCode, final Object[] args) {
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
    public SIllegalStateException(final String messageCode, final Throwable cause) {
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
    public SIllegalStateException(final String messageCode, final Object[] args,
            final Throwable cause) {
        super(MessageFormatter.getMessage(messageCode, args));
        this.messageCode = messageCode;
        this.args = args;
        this.initCause(cause);
    }

    /**
     * メッセージコードを返します。
     * 
     * @return メッセージコード
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * メッセージに埋め込まれる引数を返します。
     * 
     * @return メッセージに埋め込まれる引数
     */
    public Object[] getArgs() {
        return args;
    }
}
