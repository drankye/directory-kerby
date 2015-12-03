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
package org.apache.kerby.kerberos.kerb.type.pa.otp;

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.type.Asn1Integer;
import org.apache.kerby.asn1.type.Asn1OctetString;
import org.apache.kerby.asn1.type.Asn1Utf8String;
import org.apache.kerby.asn1.ExplicitField;
import org.apache.kerby.kerberos.kerb.type.KerberosString;
import org.apache.kerby.kerberos.kerb.type.KrbSequenceType;
import org.apache.kerby.kerberos.kerb.type.pa.pkinit.AlgorithmIdentifiers;

/**
 OTP-TOKENINFO ::= SEQUENCE {
     flags            [0] OTPFlags,
     otp-vendor       [1] UTF8String               OPTIONAL,
     otp-challenge    [2] OCTET STRING (SIZE(1..MAX)) OPTIONAL,
     otp-length       [3] Int32                    OPTIONAL,
     otp-format       [4] OTPFormat                OPTIONAL,
     otp-tokenID      [5] OCTET STRING             OPTIONAL,
     otp-algID        [6] AnyURI                   OPTIONAL,
     supportedHashAlg [7] SEQUENCE OF AlgorithmIdentifier OPTIONAL,
     iterationCount   [8] Int32                    OPTIONAL
 }
 */
public class OtpTokenInfo extends KrbSequenceType {
    FLAGS = 0;
    OTP_VENDOR = 1;
    OTP_CHALLENGE = 2;
    OTP_LENGTH = 3;
    OTP_FORMAT = 4;
    OTP_TOKEN_ID = 5;
    OTP_ALG_ID = 6;
    SUPPORTED_HASH_ALG = 7;
    ITERATION_COUNT = 8;

    static Asn1FieldInfo[] fieldInfos = new Asn1FieldInfo[] {
            new ExplicitField(MyEnum.FLAGS, Asn1OctetString.class),
            new ExplicitField(MyEnum.OTP_VENDOR, Asn1Utf8String.class),
            new ExplicitField(MyEnum.OTP_CHALLENGE, Asn1OctetString.class),
            new ExplicitField(MyEnum.OTP_LENGTH, KerberosString.class),
            new ExplicitField(MyEnum.OTP_FORMAT, Asn1OctetString.class),
            new ExplicitField(MyEnum.OTP_TOKEN_ID, Asn1Utf8String.class),
            new ExplicitField(MyEnum.OTP_ALG_ID, Asn1OctetString.class),
            new ExplicitField(MyEnum.SUPPORTED_HASH_ALG, AlgorithmIdentifiers.class),
            new ExplicitField(MyEnum.ITERATION_COUNT, Asn1Integer.class)
    };

    public OtpTokenInfo() {
        super(fieldInfos);
    }
}
