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

public class DecodeBuffer {
    protected final ByteBuffer byteBuffer;
    protected final int startOffset;

    public DecodeBuffer(byte[] bytes) {
        this.byteBuffer = ByteBuffer.wrap(bytes);
        this.startOffset = 0;
    }

    public DecodeBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        this.startOffset = byteBuffer.position();
    }

    public DecodeBuffer(DecodeBuffer other) {
        this.byteBuffer = other.byteBuffer.duplicate();
        this.startOffset = byteBuffer.position();
    }

    public ByteBuffer buffer() {
        return byteBuffer;
    }

    /**
     * Check if any content left to be available for reading.
     * @return true if any content left, false otherwise
     */
    public boolean available() {
        return byteBuffer.remaining() > 0;
    }

    /**
     * Read a byte from the underlying buffer.
     * Note: please always call available() before calling this.
     *
     * @return a byte just read
     */
    public byte readByte() {
        return byteBuffer.get();
    }

    /**
     * Skip bytes of len.
     * @param len
     */
    public void skip(int len) {
        int newPos = byteBuffer.position() + len;
        byteBuffer.position(newPos);
    }

    /**
     * @return number of left bytes to be read
     */
    public int remaining() {
        return byteBuffer.remaining();
    }
}
