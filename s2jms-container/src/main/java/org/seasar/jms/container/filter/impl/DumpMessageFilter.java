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

package org.seasar.jms.container.filter.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.seasar.framework.log.Logger;
import org.seasar.jms.container.filter.Filter;
import org.seasar.jms.container.filter.FilterChain;
import org.seasar.jms.core.util.IterableAdapter;

/**
 * @author koichik
 * 
 */
public class DumpMessageFilter implements Filter {

    private static final Logger logger = Logger.getLogger(DumpMessageFilter.class);

    public void doFilter(final Message message, final FilterChain chain) throws Exception {
        if (logger.isDebugEnabled()) {
            if (TextMessage.class.isInstance(message)) {
                dumpMessage(TextMessage.class.cast(message));
            } else if (BytesMessage.class.isInstance(message)) {
                dumpMessage(BytesMessage.class.cast(message));
            } else if (MapMessage.class.isInstance(message)) {
                dumpMessage(MapMessage.class.cast(message));
            } else if (ObjectMessage.class.isInstance(message)) {
                dumpMessage(ObjectMessage.class.cast(message));
            } else {
                logger.log("DJMS-CONTAINER2107", new Object[] { message.getClass().getName(),
                        message });
            }
        }
        chain.doFilter(message);
    }

    protected void dumpMessage(final TextMessage message) throws JMSException {
        final String payload = message.getText();
        logger.log("DJMS-CONTAINER2107", new Object[] { message.getClass().getName(), payload });
    }

    protected void dumpMessage(final BytesMessage message) throws JMSException {
        int remain = (int) message.getBodyLength();
        final byte[] bytes = new byte[16];
        final StringWriter sw = new StringWriter(1000);
        final PrintWriter pw = new PrintWriter(sw);
        while (remain > 0) {
            final int length = message.readBytes(bytes);
            for (int i = 0; i < length; ++i) {
                pw.printf("%02x ", bytes[i]);
            }
            pw.println();
            remain -= length;
        }
        pw.close();
        logger.log("DJMS-CONTAINER2107", new Object[] { message.getClass().getName(),
                new String(sw.getBuffer()) });
    }

    protected void dumpMessage(final MapMessage message) throws JMSException {
        final StringBuilder buf = new StringBuilder(500);
        for (final String key : new IterableAdapter(message.getMapNames())) {
            buf.append(key).append(" : ").append(message.getObject(key)).append("\n");
        }
        logger.log("DJMS-CONTAINER2107", new Object[] { message.getClass().getName(),
                new String(buf) });
    }

    protected void dumpMessage(final ObjectMessage message) throws JMSException {
        final Object payload = message.getObject();
        logger.log("DJMS-CONTAINER2107", new Object[] { message.getClass().getName(), payload });
    }

}
