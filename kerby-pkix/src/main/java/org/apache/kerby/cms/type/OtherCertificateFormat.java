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
package org.apache.kerby.cms.type;

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.EnumType;
import org.apache.kerby.asn1.type.Asn1Any;
import org.apache.kerby.asn1.type.Asn1ObjectIdentifier;
import org.apache.kerby.asn1.type.Asn1SequenceType;
import org.apache.kerby.asn1.type.Asn1Type;

import static org.apache.kerby.cms.type.OtherCertificateFormat.MyEnum.*;

/**
 * OtherCertificateFormat ::= SEQUENCE {
 *   otherCertFormat OBJECT IDENTIFIER,
 *   otherCert ANY DEFINED BY otherCertFormat
 * }
 */
public class OtherCertificateFormat extends Asn1SequenceType {
    protected enum MyEnum implements EnumType {
        OTHER_CERT_FORMAT,
        OTHER_CERT;

        @Override
        public int getValue() {
            return ordinal();
        }

        @Override
        public String getName() {
            return name();
        }
    }

    static Asn1FieldInfo[] fieldInfos = new Asn1FieldInfo[] {
            new Asn1FieldInfo(OTHER_CERT_FORMAT, Asn1ObjectIdentifier.class),
            new Asn1FieldInfo(OTHER_CERT, Asn1Any.class),
    };

    public OtherCertificateFormat() {
        super(fieldInfos);
    }

    public Asn1ObjectIdentifier getOtherCertFormat() {
        return getFieldAs(OTHER_CERT_FORMAT, Asn1ObjectIdentifier.class);
    }

    public void setOtherCertFormat(Asn1ObjectIdentifier otherCertFormat) {
        setFieldAs(OTHER_CERT_FORMAT, otherCertFormat);
    }

    public <T extends Asn1Type> T getOtherCertAs(Class<T> t) {
        return getFieldAsAny(OTHER_CERT, t);
    }

    public void setOtherCert(Asn1Type otherCert) {
        setFieldAsAny(OTHER_CERT, otherCert);
    }
}
