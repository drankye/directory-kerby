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

public class EndFlagBuffer extends DecodeBuffer {
    private LimitedBuffer limitedBuffer;

    public EndFlagBuffer(byte[] bytes) {
        this(ByteBuffer.wrap(bytes));
    }

    public EndFlagBuffer(DecodeBuffer other) {
        this(other.byteBuffer.duplicate());
    }

    public EndFlagBuffer(ByteBuffer byteBuffer) {
        super(byteBuffer);

        int endFlagsPos = findEndFlags(byteBuffer.duplicate());
        if (endFlagsPos < 1) {
            throw new IllegalArgumentException("Not ended with 00 bytes");
        }

        int limit = endFlagsPos - 1;
        this.limitedBuffer = new LimitedBuffer(byteBuffer, limit);
    }

    @Override
    public boolean available() {
        return limitedBuffer.available();
    }

    @Override
    public byte readByte() {
        return limitedBuffer.readByte();
    }

    @Override
    public byte[] readAllLeftBytes() {
        return limitedBuffer.readAllLeftBytes();
    }

    @Override
    public byte[] readBytes(int len) {
        return limitedBuffer.readBytes(len);
    }

    @Override
    public void readBytes(byte[] bytes) {
        limitedBuffer.readBytes(bytes);
    }

    @Override
    public int hasRead() {
        return limitedBuffer.hasRead();
    }

    /**
     * @return the positon of the end flags as normal content limit.
     */
    private int findEndFlags(ByteBuffer buffer) {
        if (byteBuffer.remaining() < 2) {
            return -1;
        }

        byte flagByte1 = byteBuffer.get();
        byte flagByte2 = byteBuffer.get();

        while (true) {
            if (flagByte1 == 0x00 && flagByte2 == 0x00) {
                return buffer.position();
            }
            if (byteBuffer.remaining() > 0) {
                flagByte1 = flagByte2;
                flagByte2 = byteBuffer.get();
            } else {
                return -1;
            }
        }
    }
}
