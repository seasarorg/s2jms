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
package org.seasar.jms.container.unit;

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
 * @author koichik
 * 
 */
public class BytesMessageMock extends MessageMock implements BytesMessage {
    protected boolean readOnlyMode;
    protected byte[] bytes;
    protected ByteArrayOutputStream baos;
    protected DataOutputStream os;
    protected ByteArrayInputStream bais;
    protected DataInputStream is;

    public BytesMessageMock() {
        baos = new ByteArrayOutputStream();
        os = new DataOutputStream(baos);
    }

    public BytesMessageMock(final byte[] bytes) {
        this.bytes = bytes;
        readOnlyMode = true;
        bais = new ByteArrayInputStream(bytes);
        is = new DataInputStream(bais);
    }

    protected void assertReadable() throws MessageNotReadableException {
        if (!readOnlyMode) {
            throw new MessageNotReadableException("");
        }
    }

    protected void assertWritable() throws MessageNotWriteableException {
        if (readOnlyMode) {
            throw new MessageNotWriteableException("");
        }
    }

    protected JMSException newJMSException(Throwable cause) throws JMSException {
        JMSException e = new JMSException("exception occurd");
        e.initCause(cause);
        throw e;
    }

    public long getBodyLength() throws JMSException {
        assertReadable();
        return bytes.length;
    }

    public boolean readBoolean() throws JMSException {
        assertReadable();
        try {
            return is.readBoolean();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public byte readByte() throws JMSException {
        assertReadable();
        try {
            return is.readByte();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public int readUnsignedByte() throws JMSException {
        assertReadable();
        try {
            return is.readByte();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public short readShort() throws JMSException {
        assertReadable();
        try {
            return is.readShort();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public int readUnsignedShort() throws JMSException {
        assertReadable();
        try {
            return is.readShort();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public char readChar() throws JMSException {
        assertReadable();
        try {
            return is.readChar();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public int readInt() throws JMSException {
        assertReadable();
        try {
            return is.readInt();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public long readLong() throws JMSException {
        assertReadable();
        try {
            return is.readLong();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public float readFloat() throws JMSException {
        assertReadable();
        try {
            return is.readFloat();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public double readDouble() throws JMSException {
        assertReadable();
        try {
            return is.readDouble();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public String readUTF() throws JMSException {
        assertReadable();
        try {
            return is.readUTF();
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public int readBytes(byte[] bytes) throws JMSException {
        assertReadable();
        try {
            return is.read(bytes);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public int readBytes(byte[] bytes, int length) throws JMSException {
        assertReadable();
        try {
            return is.read(bytes, 0, length);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeBoolean(boolean value) throws JMSException {
        assertWritable();
        try {
            os.writeBoolean(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeByte(byte value) throws JMSException {
        assertWritable();
        try {
            os.writeByte(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeShort(short value) throws JMSException {
        assertWritable();
        try {
            os.writeShort(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeChar(char value) throws JMSException {
        assertWritable();
        try {
            os.writeChar(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeInt(int value) throws JMSException {
        assertWritable();
        try {
            os.writeInt(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeLong(long value) throws JMSException {
        assertWritable();
        try {
            os.writeLong(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeFloat(float value) throws JMSException {
        assertWritable();
        try {
            os.writeFloat(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeDouble(double value) throws JMSException {
        assertWritable();
        try {
            os.writeDouble(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeUTF(String value) throws JMSException {
        assertWritable();
        try {
            os.writeUTF(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeBytes(byte[] value) throws JMSException {
        assertWritable();
        try {
            os.write(value);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeBytes(byte[] value, int offset, int length) throws JMSException {
        assertWritable();
        try {
            os.write(value, offset, length);
        } catch (IOException e) {
            throw newJMSException(e);
        }
    }

    public void writeObject(Object value) throws JMSException {
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
        readOnlyMode = true;
        bytes = baos.toByteArray();
        is = new DataInputStream(bais);
        baos = null;
        os = null;
    }

}
