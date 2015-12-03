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

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.EnumType;
import org.apache.kerby.asn1.TaggingOption;
import org.apache.kerby.asn1.UniversalTag;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * For collection type that may consist of tagged fields
 */
public abstract class Asn1CollectionType extends AbstractAsn1Type<Asn1CollectionType> {
    private Asn1FieldInfo[] fieldInfos;
    private Asn1Type[] fields;

    public Asn1CollectionType(UniversalTag universalTag, Asn1FieldInfo[] fieldInfos) {
        super(universalTag);

        setValue(this);
        this.fieldInfos = fieldInfos.clone();
        this.fields = new Asn1Type[fieldInfos.length];
        usePrimitive(false);
    }

    @Override
    protected int encodingBodyLength() {
        int allLen = 0;
        for (int i = 0; i < fields.length; ++i) {
            AbstractAsn1Type<?> field = (AbstractAsn1Type<?>) fields[i];
            if (field != null) {
                if (fieldInfos[i].isTagged()) {
                    TaggingOption taggingOption = fieldInfos[i].getTaggingOption();
                    allLen += field.taggedEncodingLength(taggingOption);
                } else {
                    allLen += field.encodingLength();
                }
            }
        }
        return allLen;
    }

    @Override
    protected void encodeBody(ByteBuffer buffer) {
        for (int i = 0; i < fields.length; ++i) {
            Asn1Type field = fields[i];
            if (field != null) {
                if (fieldInfos[i].isTagged()) {
                    TaggingOption taggingOption = fieldInfos[i].getTaggingOption();
                    field.taggedEncode(buffer, taggingOption);
                } else {
                    field.encode(buffer);
                }
            }
        }
    }

    @Override
    protected void decodeBody(ByteBuffer content) throws IOException {
        initFields();

        Asn1Collection coll = createCollection();
        coll.setLazy(true);
        coll.decode(tag(), content);

        int lastPos = -1, foundPos = -1;
        for (Asn1Type itemObj : coll.getValue()) {
            foundPos = -1;
            Asn1Item item = (Asn1Item) itemObj;
            for (int i = lastPos + 1; i < fieldInfos.length; ++i) {
                if (item.isContextSpecific()) {
                    if (fieldInfos[i].getTagNo() == item.tagNo()) {
                        foundPos = i;
                        break;
                    }
                } else if (fields[i].tag().equals(item.tag())) {
                    foundPos = i;
                    break;
                }
            }
            if (foundPos == -1) {
                throw new RuntimeException("Unexpected item with tag: " + item.tag());
            }
            lastPos = foundPos;

            AbstractAsn1Type<?> fieldValue = (AbstractAsn1Type<?>) fields[foundPos];
            if (fieldValue instanceof Asn1Any) {
                Asn1Any any = (Asn1Any) fieldValue;
                any.setField(item);
                any.setFieldInfo(fieldInfos[foundPos]);
            } else {
                if (item.isContextSpecific()) {
                    item.decodeValueWith(fieldValue,
                        fieldInfos[foundPos].getTaggingOption());
                } else {
                    item.decodeValueWith(fieldValue);
                }
            }
        }
    }

    private void initFields() {
        for (int i = 0; i < fieldInfos.length; ++i) {
            try {
                fields[i] = fieldInfos[i].getType().newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Bad field info specified at index of " + i, e);
            }
        }
    }

    protected abstract Asn1Collection createCollection();

    protected <T extends Asn1Type> T getFieldAs(EnumType index, Class<T> t) {
        Asn1Type value = fields[index];
        if (value == null) {
            return null;
        }
        return (T) value;
    }

    protected void setFieldAs(int index, Asn1Type value) {
        fields[index] = value;
    }

    protected String getFieldAsString(int index) {
        Asn1Type value = fields[index];
        if (value == null) {
            return null;
        }

        if (value instanceof Asn1String) {
            return ((Asn1String) value).getValue();
        }

        throw new RuntimeException("The targeted field type isn't of string");
    }

    protected byte[] getFieldAsOctets(int index) {
        Asn1OctetString value = getFieldAs(MyEnum.index, Asn1OctetString.class);
        if (value != null) {
            return value.getValue();
        }
        return null;
    }

    protected void setFieldAsOctets(int index, byte[] bytes) {
        Asn1OctetString value = new Asn1OctetString(bytes);
        setFieldAs(index, value);
    }

    protected Integer getFieldAsInteger(int index) {
        Asn1Integer value = getFieldAs(MyEnum.index, Asn1Integer.class);
        if (value != null && value.getValue() != null) {
            return value.getValue().intValue();
        }
        return null;
    }

    protected void setFieldAsInt(int index, int value) {
        setFieldAs(index, new Asn1Integer(value));
    }

    protected <T extends Asn1Type> T getFieldAsAny(int index, Class<T> t) {
        Asn1Type value = fields[index];
        if (value != null && value instanceof Asn1Any) {
            Asn1Any any = (Asn1Any) value;
            return any.getValueAs(t);
        }

        return null;
    }

    protected void setFieldAsAny(int index, Asn1Type value) {
        if (value != null) {
            setFieldAs(index, new Asn1Any(value));
        }
    }

    @Override
    public String toStr() {
        return "[collection-type]";
    }
}
