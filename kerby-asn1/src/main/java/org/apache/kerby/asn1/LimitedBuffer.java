/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.kerby.asn1;

import java.nio.ByteBuffer;

public class LimitedBuffer extends DecodeBuffer {
    private final int limit;

    public LimitedBuffer(byte[] bytes) {
        super(bytes);
        this.limit = bytes.length;
    }

    public LimitedBuffer(ByteBuffer byteBuffer) {
        this(byteBuffer, byteBuffer.limit());
    }

    public LimitedBuffer(ByteBuffer byteBuffer, int limit) {
        super(byteBuffer);
        this.limit = limit;
    }

    public LimitedBuffer(DecodeBuffer other, int limit) {
        super(other);

        this.limit = limit;
    }

    @Override
    public boolean available() {
        return byteBuffer.hasRemaining()
                && byteBuffer.position() - startOffset < limit;
    }

    public int hasRead() {
        return (int) byteBuffer.position() - startOffset;
    }

    @Override
    public int remaining() {
        return limit - hasRead();
    }

    @Override
    public byte readByte() {
        return byteBuffer.get();
    }

    public byte[] readAllLeftBytes() {
        return readBytes(remaining());
    }

    public byte[] readBytes(int len) {
        checkLen(len);

        byte[] bytes = new byte[len];
        if (len > 0) {
            byteBuffer.get(bytes);
        }
        return bytes;
    }

    private void checkLen(int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Bad argument len: " + len);
        }
        if (len > 0) {
            if (!available()) {
                throw new IllegalArgumentException("Buffer EOF");
            }
            if (remaining() < len) {
                throw new IllegalArgumentException("Out of Buffer");
            }
        }
    }

    public void readBytes(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Invalid buffer");
        }

        if (remaining() < bytes.length) {
            throw new IllegalArgumentException("Too much to read");
        }

        byteBuffer.get(bytes);
    }
}
