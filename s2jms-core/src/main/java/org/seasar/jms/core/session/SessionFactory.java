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
package org.seasar.jms.core.session;

/**
 * JMSセッションを作成するコンポーネントのインタフェースです。
 * <p>
 * このコンポーネントはJMSセッションのライフサイクルを管理するためのテンプレートメソッドを提供します。JMSセッションが作成されると{@link SessionHandler#handleSession}がコールバックされます。<br>
 * コールバックメソッドの処理が終了すると、JMSセッションはクローズされます。
 * </p>
 * 
 * @author koichik
 */
public interface SessionFactory {
    /**
     * JMSセッションを作成し、{@link SessionHandler#handleSession}を呼び出した後JMSセッションをクローズします。
     * <p>
     * 引数{@code startConnection}に{@code true}が指定された場合は、JMSセッションを作成する前に{@link javax.jms.Connection#start()}が、
     * JMSセッションがクローズされた後に{@link javax.jms.Connection#stop}が呼び出されます。
     * </p>
     * 
     * @param startConnection
     *            JMSセッションを作成する前に{@link javax.jms.Connection#start()}を呼び出す必要がある場合は{@code true}、それ以外の場合は{@code false}
     * @param handler
     *            JMSセッションを処理するハンドラ
     */
    void operateSession(boolean startConnection, SessionHandler handler);
}