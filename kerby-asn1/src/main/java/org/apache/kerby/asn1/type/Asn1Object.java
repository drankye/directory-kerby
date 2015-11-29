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
package org.apache.kerby.asn1.type;

import org.apache.kerby.asn1.DecodeBuffer;
import org.apache.kerby.asn1.EndFlagBuffer;
import org.apache.kerby.asn1.LimitedBuffer;
import org.apache.kerby.asn1.TagClass;
import org.apache.kerby.asn1.TaggingOption;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The abstract ASN1 object for all the ASN1 types. It provides basic
 * encoding and decoding utilities.
 */
public abstract class Asn1Object implements Asn1Type {

    private TagClass tagClass = TagClass.UNKNOWN;
    private int tagNo = -1;
    private int tagFlags = 0;
    private int encodingLen = -1;

    // encoding options
    private EncodingType encodingType = EncodingType.BER;
    private boolean isImplicit = true;
    private boolean isDefinitiveLength = true;

    /**
     * Constructor with a value, generally for encoding of the value
     * @param tagFlags The tag flags
     * @param tagNo The tag number
     */
    public Asn1Object(int tagFlags, int tagNo) {
        this(TagClass.fromTagFlags(tagFlags), tagNo);
        setTagFlags(tagFlags);
    }

    /**
     * Constructor with a value, generally for encoding of the value
     * @param tagClass The tag class
     * @param tagNo The tag number
     */
    public Asn1Object(TagClass tagClass, int tagNo) {
        this.tagClass = tagClass;
        this.tagNo = tagNo;

        this.tagFlags |= tagClass.getValue();
    }

    @Override
    public TagClass tagClass() {
        return tagClass;
    }

    protected void setTagClass(TagClass tagClass) {
        this.tagClass = tagClass;
    }

    @Override
    public int tagFlags() {
        return tagFlags;
    }

    @Override
    public int tagNo() {
        return tagNo;
    }

    protected void setTagFlags(int tagFlags) {
        this.tagFlags = tagFlags & 0xe0;
    }

    protected void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    @Override
    public void usePrimitive(boolean isPrimitive) {
        if (isPrimitive) {
            tagFlags &= ~CONSTRUCTED_FLAG;
        } else {
            tagFlags |= CONSTRUCTED_FLAG;
        }
    }

    @Override
    public boolean isPrimitive() {
        return (tagFlags & CONSTRUCTED_FLAG) == 0;
    }

    @Override
    public void useDefinitiveLength(boolean isDefinitiveLength) {
        this.isDefinitiveLength = isDefinitiveLength;
    }

    @Override
    public boolean isDefinitiveLength() {
        return isDefinitiveLength;
    }

    @Override
    public void useImplicit(boolean isImplicit) {
        this.isImplicit = isImplicit;
    }

    @Override
    public boolean isImplicit() {
        return isImplicit;
    }

    @Override
    public void useDER() {
        this.encodingType = EncodingType.DER;
    }

    @Override
    public boolean isDER() {
        return encodingType == EncodingType.DER;
    }

    @Override
    public void useBER() {
        this.encodingType = EncodingType.BER;
    }

    @Override
    public boolean isBER() {
        return encodingType == EncodingType.BER;
    }

    @Override
    public void useCER() {
        this.encodingType = EncodingType.CER;
    }

    @Override
    public boolean isCER() {
        return encodingType == EncodingType.CER;
    }

    @Override
    public byte[] encode() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(encodingLength());
        encode(byteBuffer);
        byteBuffer.flip();
        return byteBuffer.array();
    }

    @Override
    public void encode(ByteBuffer buffer) {
        encodeTag(buffer, tagFlags(), tagNo());
        encodeLength(buffer, encodingBodyLength());
        encodeBody(buffer);
    }

    protected void encodeBody(ByteBuffer buffer) { }

    @Override
    public void decode(byte[] content) throws IOException {
        decode(new DecodeBuffer(content));
    }

    @Override
    public void decode(ByteBuffer content) throws IOException {
        decode(new DecodeBuffer(content));
    }

    @Override
    public int encodingLength() {
        if (encodingLen == -1) {
            int bodyLen = encodingBodyLength();
            encodingLen = lengthOfTagLength(tagNo()) + lengthOfBodyLength(bodyLen) + bodyLen;
        }
        return encodingLen;
    }

    public boolean isUniversal() {
        return tagClass.isUniversal();
    }

    public boolean isAppSpecific() {
        return tagClass.isAppSpecific();
    }

    public boolean isContextSpecific() {
        return tagClass.isContextSpecific();
    }

    public boolean isTagged() {
        return tagClass.isTagged();
    }

    public boolean isSimple() {
        return isUniversal() && Asn1Simple.isSimple(tagNo);
    }

    public boolean isCollection() {
        return isUniversal() && Asn1Collection.isCollection(tagNo);
    }

    protected abstract int encodingBodyLength();

    protected void decode(DecodeBuffer content) throws IOException {
        int tmpTag = readTag(content);
        int tmpTagNo = readTagNo(content, tmpTag);
        int tmpTagFlags = tmpTag & 0xe0;
        int length = readLength(content);

        DecodeBuffer tmpBuffer;
        if (length == -1) {
            useDefinitiveLength(false);
            tmpBuffer = content;
        } else {
            useDefinitiveLength(true);
            tmpBuffer = new LimitedBuffer(content, length);
        }

        decode(tmpTagFlags, tmpTagNo, tmpBuffer);
    }

    public void decode(int tagFlags, int tagNo,
                       DecodeBuffer content) throws IOException {
        if (tagClass() != TagClass.UNKNOWN && tagClass()
                != TagClass.fromTagFlags(tagFlags)) {
            throw new IOException("Unexpected tagFlags " + tagFlags
                    + ", expecting " + tagClass());
        }
        if (tagNo() != -1 && tagNo() != tagNo) {
            throw new IOException("Unexpected tagNo " + tagNo + ", "
                    + "expecting " + tagNo());
        }

        setTagClass(TagClass.fromTagFlags(tagFlags));
        setTagFlags(tagFlags);
        setTagNo(tagNo);

        decodeBody(content);
    }

    protected abstract void decodeBody(LimitedBuffer content) throws IOException;

    protected int taggedEncodingLength(TaggingOption taggingOption) {
        int taggingTagNo = taggingOption.getTagNo();
        int taggingBodyLen = taggingOption.isImplicit() ? encodingBodyLength()
                : encodingLength();
        int taggingEncodingLen = lengthOfTagLength(taggingTagNo)
                + lengthOfBodyLength(taggingBodyLen) + taggingBodyLen;
        return taggingEncodingLen;
    }

    public byte[] taggedEncode(TaggingOption taggingOption) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(taggedEncodingLength(taggingOption));
        taggedEncode(byteBuffer, taggingOption);
        byteBuffer.flip();
        return byteBuffer.array();
    }

    @Override
    public void taggedEncode(ByteBuffer buffer, TaggingOption taggingOption) {
        int taggingTagFlags = taggingOption.tagFlags(!isPrimitive());
        encodeTag(buffer, taggingTagFlags, taggingOption.getTagNo());
        int taggingBodyLen = taggingOption.isImplicit() ? encodingBodyLength()
                : encodingLength();
        encodeLength(buffer, taggingBodyLen);
        if (taggingOption.isImplicit()) {
            encodeBody(buffer);
        } else {
            encode(buffer);
        }
    }

    public void taggedDecode(byte[] content,
                             TaggingOption taggingOption) throws IOException {
        taggedDecode(ByteBuffer.wrap(content), taggingOption);
    }

    @Override
    public void taggedDecode(ByteBuffer content,
                             TaggingOption taggingOption) throws IOException {
        DecodeBuffer limitedBuffer = new DecodeBuffer(content);
        taggedDecode(limitedBuffer, taggingOption);
    }

    protected void taggedDecode(DecodeBuffer content,
                                TaggingOption taggingOption) throws IOException {
        int taggingTag = readTag(content);
        int taggingTagNo = readTagNo(content, taggingTag);
        int taggingLength = readLength(content);

        DecodeBuffer tmpBuffer;
        if (taggingLength == -1) {
            useDefinitiveLength(false);
            tmpBuffer = content;
        } else {
            useDefinitiveLength(true);
            tmpBuffer = new LimitedBuffer(content, taggingLength);
        }

        int tmpTagFlags = taggingTag & 0xe0;
        taggedDecode(tmpTagFlags, taggingTagNo, tmpBuffer, taggingOption);
    }

    protected void taggedDecode(int taggingTagFlags, int taggingTagNo,
                                DecodeBuffer content,
                                TaggingOption taggingOption) throws IOException {
        int expectedTaggingTagFlags = taggingOption.tagFlags(!isPrimitive());
        if (expectedTaggingTagFlags != taggingTagFlags) {
            throw new IOException("Unexpected tag flags " + taggingTagFlags
                    + ", expecting " + expectedTaggingTagFlags);
        }
        if (taggingOption.getTagNo() != taggingTagNo) {
            throw new IOException("Unexpected tagNo " + taggingTagNo + ", "
                    + "expecting " + taggingOption.getTagNo());
        }

        if (taggingOption.isImplicit()) {
            decodeBody(content);
        } else {
            decode(content);
        }
    }

    public static Asn1Item decodeOne(DecodeBuffer content) throws IOException {
        int tag = readTag(content);
        int tagNo = readTagNo(content, tag);
        int length = readLength(content);

        DecodeBuffer tmpBuffer;
        if (length == -1) {
            tmpBuffer = content;
        } else {
            tmpBuffer = new LimitedBuffer(content, length);
        }

        content.skip(length);

        Asn1Item result = new Asn1Item(tag, tagNo, valueContent);
        if (result.isSimple()) {
            result.decodeValueAsSimple();
        }
        return result;
    }

    public static void skipOne(DecodeBuffer content) throws IOException {
        int tag = readTag(content);
        readTagNo(content, tag);
        int length = readLength(content);

        int lengthForSkip = length;
        if (length == -1) {
            EndFlagBuffer tmpBuffer = new EndFlagBuffer(content);
            lengthForSkip = tmpBuffer.skipToEnd();
        }
        content.skip(lengthForSkip);
    }

    public static int lengthOfBodyLength(int bodyLength) {
        int length = 1;

        if (bodyLength > 127) {
            int payload = bodyLength;
            while (payload != 0) {
                payload >>= 8;
                length++;
            }
        }

        return length;
    }

    public static int lengthOfTagLength(int tagNo) {
        int length = 1;

        if (tagNo >= 31) {
            if (tagNo < 128) {
                length++;
            } else {
                length++;

                do {
                    tagNo >>= 7;
                    length++;
                } while (tagNo > 127);
            }
        }

        return length;
    }

    public static void encodeTag(ByteBuffer buffer, int flags, int tagNo) {
        if (tagNo < 31) {
            buffer.put((byte) (flags | tagNo));
        } else {
            buffer.put((byte) (flags | 0x1f));
            if (tagNo < 128) {
                buffer.put((byte) tagNo);
            } else {
                byte[] tmpBytes = new byte[5]; // 5 * 7 > 32
                int iPut = tmpBytes.length;

                tmpBytes[--iPut] = (byte) (tagNo & 0x7f);
                do {
                    tagNo >>= 7;
                    tmpBytes[--iPut] = (byte) (tagNo & 0x7f | 0x80);
                } while (tagNo > 127);

                buffer.put(tmpBytes, iPut, tmpBytes.length - iPut);
            }
        }
    }

    public static void encodeLength(ByteBuffer buffer, int bodyLength) {
        if (bodyLength < 128) {
            buffer.put((byte) bodyLength);
        } else {
            int length = 0;
            int payload = bodyLength;

            while (payload != 0) {
                payload >>= 8;
                length++;
            }

            buffer.put((byte) (length | 0x80));

            payload = bodyLength;
            for (int i = length - 1; i >= 0; i--) {
                buffer.put((byte) (payload >> (i * 8)));
            }
        }
    }

    public static int readTag(DecodeBuffer buffer) throws IOException {
        int tag = buffer.readByte() & 0xff;
        if (tag == 0) {
            throw new IOException("Bad tag 0 found");
        }
        return tag;
    }

    public static int readTagNo(DecodeBuffer buffer, int tag) throws IOException {
        int tagNo = tag & 0x1f;

        if (tagNo == 0x1f) {
            tagNo = 0;

            int b = buffer.readByte() & 0xff;
            if ((b & 0x7f) == 0) {
                throw new IOException("Invalid high tag number found");
            }

            while (b >= 0 && (b & 0x80) != 0) {
                tagNo |= b & 0x7f;
                tagNo <<= 7;
                b = buffer.readByte();
            }

            tagNo |= b & 0x7f;
        }

        return tagNo;
    }

    public static int readLength(DecodeBuffer buffer) throws IOException {
        int result = buffer.readByte() & 0xff;
        if (result == 0x80) {
            return -1; // non-definitive length
        }

        if (result > 127) {
            int length = result & 0x7f;
            if (length > 4) {
                throw new IOException("Bad length of more than 4 bytes: " + length);
            }

            result = 0;
            int tmp;
            for (int i = 0; i < length; i++) {
                tmp = buffer.readByte() & 0xff;
                result = (result << 8) + tmp;
            }
        }

        if (result < 0) {
            throw new IOException("Invalid length " + result);
        }

        if (result > buffer.remaining()) {
            throw new IOException("Corrupt stream - less data "
                + buffer.remaining() + " than expected " + result);
        }

        return result;
    }
}
