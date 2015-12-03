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
package org.apache.kerby.kerberos.kerb.type.fast;

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.ExplicitField;
import org.apache.kerby.kerberos.kerb.type.KrbSequenceType;
import org.apache.kerby.kerberos.kerb.type.base.CheckSum;
import org.apache.kerby.kerberos.kerb.type.base.EncryptedData;
import org.apache.kerby.kerberos.kerb.type.pa.PaData;

/**
 KrbFastFinished ::= SEQUENCE {
     timestamp       [0] KerberosTime,
     usec            [1] Microseconds,
     -- timestamp and usec represent the time on the KDC when
     -- the reply was generated.
     crealm          [2] Realm,
     cname           [3] PrincipalName,
     -- Contains the client realm and the client name.
     ticket-checksum [4] Checksum,
     -- checksum of the ticket in the KDC-REP using the armor
     -- and the key usage is KEY_USAGE_FAST_FINISH.
     -- The checksum type is the required checksum type
     -- of the armor key.
 }
 */
public class KrbFastFinished extends KrbSequenceType {
    FAST_OPTIONS = 0;
    PADATA = 1;
    REQ_BODY = 2;

    static Asn1FieldInfo[] fieldInfos = new Asn1FieldInfo[] {
            new ExplicitField(MyEnum.FAST_OPTIONS, KrbFastArmor.class),
            new ExplicitField(MyEnum.PADATA, PaData.class),
            new ExplicitField(MyEnum.REQ_BODY, EncryptedData.class),
    };

    public KrbFastFinished() {
        super(fieldInfos);
    }

    public KrbFastArmor getArmor() {
        return getFieldAs(MyEnum.FAST_OPTIONS, KrbFastArmor.class);
    }

    public void setArmor(KrbFastArmor armor) {
        setFieldAs(FAST_OPTIONS, armor);
    }

    public CheckSum getReqChecksum() {
        return getFieldAs(MyEnum.PADATA, CheckSum.class);
    }

    public void setReqChecksum(CheckSum checkSum) {
        setFieldAs(PADATA, checkSum);
    }

    public EncryptedData getEncFastReq() {
        return getFieldAs(MyEnum.REQ_BODY, EncryptedData.class);
    }

    public void setEncFastReq(EncryptedData encFastReq) {
        setFieldAs(REQ_BODY, encFastReq);
    }
}
