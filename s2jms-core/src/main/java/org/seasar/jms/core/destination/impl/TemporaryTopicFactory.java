/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * JMSセッションから一時的な{@link javax.jms.Topic トピック}を作成するコンポーネントです。
 * <p>
 * このコンポーネントはJMSセッションからデスティネーションを作成するため、通常JMSセッション毎に異なったインスタンスを生成する必要があります。
 * </p>
 * 
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class TemporaryTopicFactory extends AbstractDestinationFactory {

    /**
     * インスタンスを構築します。
     */
    public TemporaryTopicFactory() {
    }

    /**
     * JMSセッションから一時的なトピックを作成して返します。
     * <p>
     * このメソッドは{@link org.seasar.jms.core.destination.impl.AbstractDestinationFactory#getDestination}が
     * 最初に呼び出された時に一度だけ呼び出されます。
     * </p>
     * 
     * @param session
     *            JMSセッション
     * @return JMSデスティネーション({@link javax.jms.Topic})
     */
    @Override
    protected Topic createDestination(final Session session) throws JMSException {
        return session.createTemporaryTopic();
    }

}
