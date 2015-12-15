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

import org.apache.kerby.asn1.Tag;
import org.apache.kerby.asn1.TaggingOption;
import org.apache.kerby.asn1.UniversalTag;
import org.apache.kerby.asn1.parse.Asn1Container;
import org.apache.kerby.asn1.parse.Asn1DerivedItem;
import org.apache.kerby.asn1.parse.Asn1ParseResult;
import org.apache.kerby.asn1.parse.Asn1Parser;
import org.apache.kerby.asn1.util.Asn1Util;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The abstract ASN1 object for all the ASN1 types. It provides basic
 * encoding and decoding utilities.
 */
public abstract class Asn1Encodeable extends Asn1Object implements Asn1Type {

    private int bodyLength = -1;

    // encoding options
    private EncodingType encodingType = EncodingType.BER;
    private boolean isImplicit = true;
    private boolean isDefinitiveLength = true; // by default!!

    /**
     * Constructor with a tag
     * @param tag The tag
     */
    public Asn1Encodeable(Tag tag) {
        super(tag);
    }

    /**
     * Default constructor with an universal tag.
     * @param tag the tag
     */
    public Asn1Encodeable(UniversalTag tag) {
        super(tag);
    }

    /**
     * Constructor with a tag
     * @param tag The tag
     */
    public Asn1Encodeable(int tag) {
        super(tag);
    }

    @Override
    public void usePrimitive(boolean isPrimitive) {
        tag().usePrimitive(isPrimitive);
    }

    @Override
    public boolean isPrimitive() {
        return tag().isPrimitive();
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
        Asn1Util.encodeTag(buffer, tag());
        Asn1Util.encodeLength(buffer, encodingBodyLength());
        encodeBody(buffer);
    }

    protected void encodeBody(ByteBuffer buffer) { }

    @Override
    public void decode(byte[] content) throws IOException {
        decode(ByteBuffer.wrap(content));
    }

    @Override
    public int encodingLength() {
        return encodingHeaderLength() + getBodyLength();
    }

    private int getBodyLength() {
        if (bodyLength == -1) {
            bodyLength = encodingBodyLength();
        }
        return bodyLength;
    }

    protected int encodingHeaderLength() {
        int bodyLen = getBodyLength();
        int headerLen = Asn1Util.lengthOfTagLength(tagNo());
        headerLen += (isDefinitiveLength()
            ? Asn1Util.lengthOfBodyLength(bodyLen) : 1);
        return headerLen;
    }

    protected abstract int encodingBodyLength();

    @Override
    public void decode(ByteBuffer content) throws IOException {
        Asn1ParseResult parseResult = Asn1Parser.parse(content);
        decode(parseResult);
    }

    public void decode(Asn1ParseResult parseResult) throws IOException {
        if (!tag().equals(parseResult.tag())) {
            // Primitive but using constructed encoding
            if (isPrimitive() && !parseResult.isPrimitive()) {
                Asn1Container container = (Asn1Container) parseResult;
                parseResult = new Asn1DerivedItem(tag(), container);
            } else {
                throw new IOException("Unexpected item " + parseResult.typeStr()
                    + ", expecting " + tag());
            }
        }

        decodeBody(parseResult);
    }

    protected abstract void decodeBody(Asn1ParseResult parseResult) throws IOException;

    protected int taggedEncodingLength(TaggingOption taggingOption) {
        int taggingTagNo = taggingOption.getTagNo();
        int taggingBodyLen = taggingOption.isImplicit() ? encodingBodyLength()
                : encodingLength();
        int taggingEncodingLen = Asn1Util.lengthOfTagLength(taggingTagNo)
                + Asn1Util.lengthOfBodyLength(taggingBodyLen) + taggingBodyLen;
        return taggingEncodingLen;
    }

    @Override
    public byte[] taggedEncode(TaggingOption taggingOption) {
        int len = taggedEncodingLength(taggingOption);
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        taggedEncode(byteBuffer, taggingOption);
        byteBuffer.flip();
        return byteBuffer.array();
    }

    @Override
    public void taggedEncode(ByteBuffer buffer, TaggingOption taggingOption) {
        Tag taggingTag = taggingOption.getTag(!isPrimitive());
        Asn1Util.encodeTag(buffer, taggingTag);
        int taggingBodyLen = taggingOption.isImplicit() ? encodingBodyLength()
                : encodingLength();
        Asn1Util.encodeLength(buffer, taggingBodyLen);
        if (taggingOption.isImplicit()) {
            encodeBody(buffer);
        } else {
            encode(buffer);
        }
    }

    @Override
    public void taggedDecode(byte[] content,
                             TaggingOption taggingOption) throws IOException {
        taggedDecode(ByteBuffer.wrap(content), taggingOption);
    }

    @Override
    public void taggedDecode(ByteBuffer content,
                             TaggingOption taggingOption) throws IOException {
        Asn1ParseResult parseResult = Asn1Parser.parse(content);
        taggedDecode(parseResult, taggingOption);
    }

    public void taggedDecode(Asn1ParseResult parseResult,
                                TaggingOption taggingOption) throws IOException {
        Tag expectedTaggingTagFlags = taggingOption.getTag(!isPrimitive());
        if (!expectedTaggingTagFlags.equals(parseResult.tag())) {
            throw new IOException("Unexpected tag " + parseResult.tag()
                    + ", expecting " + expectedTaggingTagFlags);
        }

        if (taggingOption.isImplicit()) {
            decodeBody(parseResult);
        } else {
            Asn1Container container = (Asn1Container) parseResult;
            Asn1ParseResult body = container.getChildren().get(0);
            decode(body);
        }
    }
}
