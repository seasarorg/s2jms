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
package org.seasar.jms.core.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;

/**
 * {@link javax.jms.BytesMessage}のモックです。
 * <p>
 * {@link javax.jms.BytesMessage}は二つの状態を持ちます。
 * <dl>
 * <dt>writable</dt>
 * <dd>メッセージにデータを設定できる状態です</dd>
 * <dt>readable</dt>
 * <dd>メッセージからデータを読み出せる状態です</dd>
 * </dl>
 * モックオブジェクトをデフォルトコンストラクタで構築するとwritable状態のインスタンスが作成されます。 データを設定した後、
 * {@link #reset()}メソッドを呼び出すことにより、 readable状態に遷移します。<br>
 * モックオブジェクトを{@link #BytesMessageMock(byte[])}で構築するとreadable状態のインスタンスが作成されます。
 * </p>
 * 
 * @author koichik
 */
public class BytesMessageMock extends MessageMock implements BytesMessage {

    /**
     * BytesMessageMockの状態を表す列挙
     */
    public enum State {
        /** メッセージにデータを設定できる状態 */
        WRITABLE,

        /** メッセージからデータを読み出せる状態 */
        READABLE
    };

    /** 状態 */
    protected State state;

    /** JMSメッセージのペイロード */
    protected byte[] bytes;

    /** 状態が{@link State#WRITABLE}の場合に{@link #bytes}に書き込むための出力ストリーム */
    protected ByteArrayOutputStream baos;

    /** 状態が{@link State#WRITABLE}の場合に{@link #bytes}に書き込むための出力ストリーム */
    protected DataOutputStream os;

    /** 状態が{@link State#READABLE}の場合に{@link #bytes}から読み込むための入力ストリーム */
    protected ByteArrayInputStream bais;

    /** 状態が{@link State#READABLE}の場合に{@link #bytes}から読み込むための入力ストリーム */
    protected DataInputStream is;

    /**
     * writable状態のインスタンスを構築します。
     */
    public BytesMessageMock() {
        state = State.WRITABLE;
        baos = new ByteArrayOutputStream();
        os = new DataOutputStream(baos);
    }

    /**
     * バイト列を指定してreadable状態のインスタンスを構築します。
     * 
     * @param bytes
     *            バイト列
     */
    public BytesMessageMock(final byte[] bytes) {
        this.bytes = bytes;
        state = State.READABLE;
        bais = new ByteArrayInputStream(bytes);
        is = new DataInputStream(bais);
    }

    /**
     * インスタンスがwritable状態であることを検証します。
     * 
     * @throws MessageNotWriteableException
     *             インスタンスがwritable状態でない場合にスローされます
     */
    protected void assertWritable() throws MessageNotWriteableException {
        if (state != State.WRITABLE) {
            throw new MessageNotWriteableException("");
        }
    }

    /**
     * インスタンスがreadable状態であることを検証します。
     * 
     * @throws MessageNotReadableException
     *             インスタンスがreadable状態でない場合にスローされます
     */
    protected void assertReadable() throws MessageNotReadableException {
        if (state != State.READABLE) {
            throw new MessageNotReadableException("");
        }
    }

    /**
     * <code>cause</code>を原因として持つ{@link JMSException}を作成してスローします。
     * 
     * @param cause
     *            原因となった例外
     * @return <code>cause</code>を原因として持つ{@link JMSException}
     */
    protected JMSException newJMSException(final Throwable cause) {
        final JMSException e = new JMSException("exception occurd");
        e.initCause(cause);
        return e;
    }

    public long getBodyLength() throws JMSException {
        assertReadable();
        return bytes.length;
    }

    public boolean readBoolean() throws JMSException {
        assertReadable();
        try {
            return is.readBoolean();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public byte readByte() throws JMSException {
        assertReadable();
        try {
            return is.readByte();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public int readUnsignedByte() throws JMSException {
        assertReadable();
        try {
            return is.readByte();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public short readShort() throws JMSException {
        assertReadable();
        try {
            return is.readShort();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public int readUnsignedShort() throws JMSException {
        assertReadable();
        try {
            return is.readShort();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public char readChar() throws JMSException {
        assertReadable();
        try {
            return is.readChar();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public int readInt() throws JMSException {
        assertReadable();
        try {
            return is.readInt();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public long readLong() throws JMSException {
        assertReadable();
        try {
            return is.readLong();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public float readFloat() throws JMSException {
        assertReadable();
        try {
            return is.readFloat();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public double readDouble() throws JMSException {
        assertReadable();
        try {
            return is.readDouble();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public String readUTF() throws JMSException {
        assertReadable();
        try {
            return is.readUTF();
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public int readBytes(final byte[] bytes) throws JMSException {
        assertReadable();
        try {
            return is.read(bytes);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public int readBytes(final byte[] bytes, final int length) throws JMSException {
        assertReadable();
        try {
            return is.read(bytes, 0, length);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeBoolean(final boolean value) throws JMSException {
        assertWritable();
        try {
            os.writeBoolean(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeByte(final byte value) throws JMSException {
        assertWritable();
        try {
            os.writeByte(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeShort(final short value) throws JMSException {
        assertWritable();
        try {
            os.writeShort(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeChar(final char value) throws JMSException {
        assertWritable();
        try {
            os.writeChar(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeInt(final int value) throws JMSException {
        assertWritable();
        try {
            os.writeInt(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeLong(final long value) throws JMSException {
        assertWritable();
        try {
            os.writeLong(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeFloat(final float value) throws JMSException {
        assertWritable();
        try {
            os.writeFloat(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeDouble(final double value) throws JMSException {
        assertWritable();
        try {
            os.writeDouble(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeUTF(final String value) throws JMSException {
        assertWritable();
        try {
            os.writeUTF(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeBytes(final byte[] value) throws JMSException {
        assertWritable();
        try {
            os.write(value);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeBytes(final byte[] value, final int offset, final int length)
            throws JMSException {
        assertWritable();
        try {
            os.write(value, offset, length);
        } catch (final IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeObject(final Object value) throws JMSException {
        assertWritable();
        if (Byte.class.isInstance(value)) {
            writeByte(Byte.class.cast(value));
        } else if (Short.class.isInstance(value)) {
            writeShort(Short.class.cast(value));
        } else if (Integer.class.isInstance(value)) {
            writeInt(Integer.class.cast(value));
        } else if (Long.class.isInstance(value)) {
            writeLong(Long.class.cast(value));
        } else if (Float.class.isInstance(value)) {
            writeFloat(Float.class.cast(value));
        } else if (Double.class.isInstance(value)) {
            writeDouble(Double.class.cast(value));
        } else if (Boolean.class.isInstance(value)) {
            writeBoolean(Boolean.class.cast(value));
        } else if (Character.class.isInstance(value)) {
            writeChar(Character.class.cast(value));
        } else if (String.class.isInstance(value)) {
            writeUTF(String.class.cast(value));
        } else if (byte[].class.isInstance(value)) {
            writeBytes(byte[].class.cast(value));
        } else {
            throw new MessageFormatException("");
        }
    }

    public void reset() throws JMSException {
        assertWritable();
        state = State.READABLE;
        bytes = baos.toByteArray();
        is = new DataInputStream(bais);
        baos = null;
        os = null;
    }

}
