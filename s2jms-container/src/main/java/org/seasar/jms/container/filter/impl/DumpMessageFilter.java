/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
import java.util.Enumeration;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.filter.Filter;
import org.seasar.jms.container.filter.FilterChain;
import org.seasar.jms.core.util.IterableAdapter;
import org.seasar.jms.core.util.JMSHeaderSupport;

/**
 * JMSメッセージの内容をログにダンプ出力するフィルタです。
 * 
 * @author koichik
 */
@Component
public class DumpMessageFilter implements Filter {

    // static fields
    private static final Logger logger = Logger.getLogger(DumpMessageFilter.class);

    // instance fields
    /** ログの出力レベルに依存せずログ出力を強制する場合に<code>true</code> */
    protected boolean forceDump = false;

    /** JMSメッセージのヘッダをダンプ出力する場合に<code>true</code> */
    protected boolean dumpHeader = true;

    /** JMSメッセージのプロパティをダンプ出力する場合に<code>true</code> */
    protected boolean dumpProperty = true;

    /**
     * ログの出力レベルに依存せずログ出力を強制する場合に<code>true</code>を設定します。
     * 
     * @param forceDump
     *            ログの出力レベルに依存せずログ出力を強制する場合に<code>true</code>
     */
    @Binding(bindingType = BindingType.MAY)
    public void setForceDump(final boolean forceDump) {
        this.forceDump = forceDump;
    }

    /**
     * JMSメッセージのヘッダをダンプ出力する場合に<code>true</code>を設定します。
     * 
     * @param dumpHeader
     *            JMSメッセージのヘッダをダンプ出力する場合に<code>true</code>
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDumpHeader(final boolean dumpHeader) {
        this.dumpHeader = dumpHeader;
    }

    /**
     * JMSメッセージのプロパティをダンプ出力する場合に<code>true</code>を設定します。
     * 
     * @param dumpProperty
     *            JMSメッセージのプロパティをダンプ出力する場合に<code>true</code>
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDumpProperty(final boolean dumpProperty) {
        this.dumpProperty = dumpProperty;
    }

    public void doFilter(final Message message, final FilterChain chain) throws Exception {
        if (forceDump || logger.isDebugEnabled()) {
            final StringBuilder buf = new StringBuilder(500);
            if (dumpHeader) {
                dumpHeader(buf, message);
            }
            if (dumpProperty) {
                dumpProperty(buf, message);
            }
            if (TextMessage.class.isInstance(message)) {
                dumpPayload(buf, TextMessage.class.cast(message));
            } else if (BytesMessage.class.isInstance(message)) {
                dumpPayload(buf, BytesMessage.class.cast(message));
            } else if (MapMessage.class.isInstance(message)) {
                dumpPayload(buf, MapMessage.class.cast(message));
            } else if (ObjectMessage.class.isInstance(message)) {
                dumpPayload(buf, ObjectMessage.class.cast(message));
            }
            logger.log("DJMS-CONTAINER2107", new Object[] { message.getClass().getName(),
                    new String(buf) });
        }
        chain.doFilter(message);
    }

    /**
     * JMSメッセージのヘッダをダンプ出力します。
     * 
     * @param buf
     *            編集用の文字列バッファ
     * @param message
     *            JMSメッセージ
     */
    protected void dumpHeader(final StringBuilder buf, final Message message) {
        buf.append("===== Headers =====\n");
        for (final String key : JMSHeaderSupport.getNames()) {
            final Object value = JMSHeaderSupport.getValue(message, key);
            buf.append(key).append(" : ").append(value).append("\n");
        }
        buf.append("\n");
    }

    /**
     * JMSメッセージのプロパティをダンプ出力します。
     * 
     * @param buf
     *            編集用の文字列バッファ
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             例外が発生した場合にスローされます
     */
    @SuppressWarnings("unchecked")
    private void dumpProperty(final StringBuilder buf, final Message message) throws JMSException {
        final Enumeration propertyNames = message.getPropertyNames();
        if (propertyNames.hasMoreElements()) {
            buf.append("===== Properties =====\n");
            for (final String key : new IterableAdapter(propertyNames)) {
                final Object value = message.getObjectProperty(key);
                buf.append(key).append(" : ").append(value).append("\n");
            }
            buf.append("\n");
        }
    }

    /**
     * {@link TextMessage}のペイロードをダンプ出力します。
     * 
     * @param buf
     *            編集用の文字列バッファ
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             例外が発生した場合にスローされます
     */
    protected void dumpPayload(final StringBuilder buf, final TextMessage message)
            throws JMSException {
        buf.append("===== Payload =====\n");
        final String payload = message.getText();
        buf.append(payload).append("\n");
    }

    /**
     * {@link BytesMessage}のペイロードをダンプ出力します。
     * 
     * @param buf
     *            編集用の文字列バッファ
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             例外が発生した場合にスローされます
     */
    protected void dumpPayload(final StringBuilder buf, final BytesMessage message)
            throws JMSException {
        buf.append("===== Payload =====\n");
        int remain = (int) message.getBodyLength();
        final byte[] bytes = new byte[16];
        final StringWriter sw = new StringWriter(1000);
        final PrintWriter pw = new PrintWriter(sw);
        while (remain > 0) {
            final int length = message.readBytes(bytes);
            for (int i = 0; i < length; ++i) {
                if (i == 8) {
                    pw.print(" ");
                }
                pw.printf("%02X ", bytes[i]);
            }
            for (int i = length; i < 16; ++i) {
                pw.print("   ");
                if (i == 8) {
                    pw.print(" ");
                }
            }
            pw.print("  ");
            for (int i = 0; i < length; ++i) {
                if (bytes[i] > 0x20 && bytes[i] < 0x80) {
                    pw.print((char) bytes[i]);
                } else {
                    pw.print('.');
                }
            }
            pw.println();
            remain -= length;
        }
        pw.close();
        buf.append(sw.getBuffer()).append("\n");
    }

    /**
     * {@link MapMessage}のペイロードをダンプ出力します。
     * 
     * @param buf
     *            編集用の文字列バッファ
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             例外が発生した場合にスローされます
     */
    protected void dumpPayload(final StringBuilder buf, final MapMessage message)
            throws JMSException {
        buf.append("===== Payload =====\n");
        for (final String key : new IterableAdapter(message.getMapNames())) {
            final Object value = message.getObject(key);
            buf.append(key).append(" : ").append(value).append("\n");
        }
        buf.append("\n");
    }

    /**
     * {@link ObjectMessage}のペイロードをダンプ出力します。
     * 
     * @param buf
     *            編集用の文字列バッファ
     * @param message
     *            JMSメッセージ
     * @throws JMSException
     *             例外が発生した場合にスローされます
     */
    protected void dumpPayload(final StringBuilder buf, final ObjectMessage message)
            throws JMSException {
        buf.append("===== Payload =====\n");
        final Object payload = message.getObject();
        buf.append(payload).append("\n");
    }

}
