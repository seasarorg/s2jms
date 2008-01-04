/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * JMSセッションを処理するコンポーネントのインタフェースです。
 * <p>
 * このコンポーネントは{@link SessionFactory}によって作成されたJMSセッションを処理するためにコールバックされます。
 * {@link #handleSession}メソッドの処理が終わってリターンするとJMSセッションはクローズされます。
 * </p>
 * 
 * @author koichik
 */
public interface SessionHandler {

    /**
     * JMSセッションを処理します。このメソッドがリターンするとJMSセッションはクローズされます。
     * 
     * @param session
     *            JMSセッション
     * @throws JMSException
     *             JMSセッションの処理中に障害が発生した場合にスローされます
     */
    void handleSession(Session session) throws JMSException;

}
