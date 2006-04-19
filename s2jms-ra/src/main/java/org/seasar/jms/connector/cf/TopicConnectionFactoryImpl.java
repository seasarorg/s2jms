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
package org.seasar.jms.connector.cf;

import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

/**
 * @author koichik
 */
public class TopicConnectionFactoryImpl extends ConnectionFactoryImpl implements
        TopicConnectionFactory {
    public TopicConnectionFactoryImpl(final ManagedConnectionFactory mcf, final ConnectionManager cm) {
        super(mcf, cm);
    }

    public TopicConnection createTopicConnection() throws JMSException {
        return (TopicConnection) createConnection();
    }

    public TopicConnection createTopicConnection(final String user, final String password)
            throws JMSException {
        return (TopicConnection) createConnection(user, password);
    }
}
