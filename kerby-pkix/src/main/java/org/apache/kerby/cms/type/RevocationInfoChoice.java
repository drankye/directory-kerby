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
import org.apache.kerby.asn1.ImplicitField;
import org.apache.kerby.asn1.type.Asn1Choice;
import org.apache.kerby.x509.type.CertificateList;

import static org.apache.kerby.cms.type.RevocationInfoChoice.MyEnum.*;

/**
 * RevocationInfoChoice ::= CHOICE {
 *   crl CertificateList,
 *   other [1] IMPLICIT OtherRevocationInfoFormat
 * }
 */
public class RevocationInfoChoice extends Asn1Choice {
    protected enum MyEnum implements EnumType {
        CRL,
        OTHER;

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
            new Asn1FieldInfo(CRL, CertificateList.class),
            new ImplicitField(OTHER, 1, OtherRevocationInfoFormat.class)
    };

    public RevocationInfoChoice() {
        super(fieldInfos);
    }

    public CertificateList getCRL() {
        return getChoiceValueAs(CRL, CertificateList.class);
    }

    public void setCRL(CertificateList crl) {
        setChoiceValue(CRL, crl);
    }

    public OtherRevocationInfoFormat getOther() {
        return getChoiceValueAs(OTHER, OtherRevocationInfoFormat.class);
    }

    public void setOther(OtherRevocationInfoFormat other) {
        setChoiceValue(OTHER, other);
    }
}
