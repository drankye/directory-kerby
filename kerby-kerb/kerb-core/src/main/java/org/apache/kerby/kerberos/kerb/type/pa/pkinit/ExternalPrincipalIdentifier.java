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
package org.apache.kerby.kerberos.kerb.type.pa.pkinit;

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.EnumType;
import org.apache.kerby.asn1.ImplicitField;
import org.apache.kerby.asn1.type.Asn1OctetString;
import org.apache.kerby.kerberos.kerb.type.KrbSequenceType;

import static org.apache.kerby.kerberos.kerb.type.pa.pkinit.ExternalPrincipalIdentifier.MyEnum.*;

/**
 ExternalPrincipalIdentifier ::= SEQUENCE {
     subjectName             [0] IMPLICIT OCTET STRING OPTIONAL,
     issuerAndSerialNumber   [1] IMPLICIT OCTET STRING OPTIONAL,
     subjectKeyIdentifier    [2] IMPLICIT OCTET STRING OPTIONAL
 }
 */
public class ExternalPrincipalIdentifier extends KrbSequenceType {
    protected enum MyEnum implements EnumType {
        SUBJECT_NAME,
        ISSUER_AND_SERIAL_NUMBER,
        SUBJECT_KEY_IDENTIFIER;

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
            new ImplicitField(SUBJECT_NAME, Asn1OctetString.class),
            new ImplicitField(ISSUER_AND_SERIAL_NUMBER, Asn1OctetString.class),
            new ImplicitField(SUBJECT_KEY_IDENTIFIER, Asn1OctetString.class)
    };

    public ExternalPrincipalIdentifier() {
        super(fieldInfos);
    }

    public byte[] getSubjectName() {
        return getFieldAsOctets(SUBJECT_NAME);
    }

    public void setSubjectName(byte[] subjectName) {
        setFieldAsOctets(SUBJECT_NAME, subjectName);
    }

    public byte[] getIssuerSerialNumber() {
        return getFieldAsOctets(ISSUER_AND_SERIAL_NUMBER);
    }

    public void setIssuerSerialNumber(byte[] issuerSerialNumber) {
        setFieldAsOctets(ISSUER_AND_SERIAL_NUMBER, issuerSerialNumber);
    }

    public byte[] getSubjectKeyIdentifier() {
        return getFieldAsOctets(SUBJECT_KEY_IDENTIFIER);
    }

    public void setSubjectKeyIdentifier(byte[] subjectKeyIdentifier) {
        setFieldAsOctets(SUBJECT_KEY_IDENTIFIER, subjectKeyIdentifier);
    }
}
