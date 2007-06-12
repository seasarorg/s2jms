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
package org.seasar.jms.core.destination.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.seasar.jms.core.destination.DestinationFactory;
import org.seasar.jms.core.exception.SJMSRuntimeException;

/**
 * JMSデスティネーション(キューまたはトピック)を作成するコンポーネントの抽象クラスです。
 * <p>
 * この実装は一度作成したデスティネーションをインスタンスに保持します。
 * このため、JMSデスティネーション一つにつき一つのインスタンスが作成されるように構成する必要があります。<br>
 * JNDIからJMSデスティネーションをルックアップするようなサブクラスのインスタンスモードはSINGLETONにすることができますが、
 * JMSセッション毎あるいはJMSメッセージ毎にインスタンスが作成されるように構成する必要があるサブクラスもあります。
 * </p>
 * 
 * @author koichik
 */
public abstract class AbstractDestinationFactory implements DestinationFactory {

    /** JMSデスティネーション */
    protected Destination destination;

    /**
     * インスタンスを構築します。
     * 
     */
    public AbstractDestinationFactory() {
    }

    /**
     * JMSデスティネーションを返します。
     * <p>
     * このメソッドが最初に呼び出された場合はサブクラスによって実装される{@link #createDestination}を
     * 呼び出してJMSデスティネーションを作成します。<br>
     * 2回目以降の呼び出しでは最初の呼び出しで作成されたJMSデスティネーションを返します。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSデスティネーション
     * @throws SJMSRuntimeException
     *             {@link javax.jms.JMSException}が発生した場合にスローされます
     */
    public Destination getDestination(final Session session) {
        if (destination == null) {
            try {
                destination = createDestination(session);
            } catch (final JMSException e) {
                throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
            }
        }
        return destination;
    }

    /**
     * JMSデスティネーションを作成して返します。
     * <p>
     * このメソッドは{@link #getDestination}が最初に呼び出された時に一度だけ呼び出されます。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSデスティネーション
     * @throws JMSException
     *             JMSの操作で例外が発生した場合にスローされます
     */
    protected abstract Destination createDestination(final Session session) throws JMSException;
}
