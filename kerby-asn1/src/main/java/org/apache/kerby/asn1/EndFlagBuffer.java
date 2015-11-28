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
    private byte flagByte1 = -1;
    private byte flagByte2 = -1;

    public EndFlagBuffer(byte[] bytes) {
        this(ByteBuffer.wrap(bytes));
    }

    public EndFlagBuffer(DecodeBuffer other) {
        this(other.byteBuffer.duplicate());
    }

    public EndFlagBuffer(ByteBuffer byteBuffer) {
        super(byteBuffer);

        if (byteBuffer.remaining() < 2) {
            throw new IllegalArgumentException(
                "Invalid buffer of less than 2 bytes");
        }

        flagByte1 = byteBuffer.get();
        flagByte2 = byteBuffer.get();
    }

    @Override
    public boolean available() {
        return flagByte1 == 0x00 && flagByte2 == 0x00;
    }

    @Override
    public byte readByte() {
        byte result = flagByte1;
        flagByte1 = flagByte2;
        if (byteBuffer.remaining() > 0) {
            flagByte2 = byteBuffer.get();
        } else {
            throw new RuntimeException("Buffer overflow");
        }

        return result;
    }

    public long hasRead() {
        return byteBuffer.position() - startOffset;
    }

    /**
     * Skip the buffer content to the end, not including the end-flag bytes.
     * @return the number of content bytes skipped
     */
    public int skipToEnd() {
        int skipped = 0;
        while (available()) {
            readByte();
            skipped++;
        }

        return skipped;
    }
}
