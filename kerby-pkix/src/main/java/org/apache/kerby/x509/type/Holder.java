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
package org.apache.kerby.x509.type;

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.type.Asn1SequenceType;
import org.apache.kerby.asn1.ExplicitField;

/**
 * <pre>
 *            Holder ::= SEQUENCE {
 *                  baseCertificateID   [0] IssuerSerial OPTIONAL,
 *                           -- the issuer and serial number of
 *                           -- the holder's Public Key Certificate
 *                  entityName          [1] GeneralNames OPTIONAL,
 *                           -- the name of the claimant or role
 *                  objectDigestInfo    [2] ObjectDigestInfo OPTIONAL
 *                           -- used to directly authenticate the holder,
 *                           -- for example, an executable
 *            }
 * </pre>
 */
public class Holder extends Asn1SequenceType {
    BASE_CERTIFICATE_ID = 0;
    ENTITY_NAME = 1;
    OBJECT_DIGEST_INFO = 2;

    static Asn1FieldInfo[] fieldInfos = new Asn1FieldInfo[] {
        new ExplicitField(MyEnum.BASE_CERTIFICATE_ID, IssuerSerial.class),
        new ExplicitField(MyEnum.ENTITY_NAME, GeneralNames.class),
        new ExplicitField(MyEnum.OBJECT_DIGEST_INFO, ObjectDigestInfo.class)
    };

    public Holder() {
        super(fieldInfos);
    }

    public IssuerSerial getBaseCertificateID() {
        return getFieldAs(MyEnum.BASE_CERTIFICATE_ID, IssuerSerial.class);
    }

    public void setBaseCertificateId(IssuerSerial baseCertificateId) {
        setFieldAs(BASE_CERTIFICATE_ID, baseCertificateId);
    }

    public GeneralNames getEntityName() {
        return getFieldAs(MyEnum.ENTITY_NAME, GeneralNames.class);
    }

    public void setEntityName(GeneralNames entityName) {
        setFieldAs(ENTITY_NAME, entityName);
    }

    public ObjectDigestInfo getObjectDigestInfo() {
        return getFieldAs(MyEnum.OBJECT_DIGEST_INFO, ObjectDigestInfo.class);
    }

    public void setObjectDigestInfo(ObjectDigestInfo objectDigestInfo) {
        setFieldAs(OBJECT_DIGEST_INFO, objectDigestInfo);
    }
}
