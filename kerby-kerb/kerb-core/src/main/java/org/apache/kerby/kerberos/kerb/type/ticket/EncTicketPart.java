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
package org.apache.kerby.kerberos.kerb.type.ticket;

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.ExplicitField;
import org.apache.kerby.kerberos.kerb.type.KerberosString;
import org.apache.kerby.kerberos.kerb.type.KerberosTime;
import org.apache.kerby.kerberos.kerb.type.KrbAppSequenceType;
import org.apache.kerby.kerberos.kerb.type.ad.AuthorizationData;
import org.apache.kerby.kerberos.kerb.type.base.EncryptionKey;
import org.apache.kerby.kerberos.kerb.type.base.HostAddresses;
import org.apache.kerby.kerberos.kerb.type.base.PrincipalName;
import org.apache.kerby.kerberos.kerb.type.base.TransitedEncoding;

/**
 -- Encrypted part of ticket
 EncTicketPart   ::= [APPLICATION 3] SEQUENCE {
 flags                   [0] TicketFlags,
 key                     [1] EncryptionKey,
 crealm                  [2] Realm,
 cname                   [3] PrincipalName,
 transited               [4] TransitedEncoding,
 authtime                [5] KerberosTime,
 starttime               [6] KerberosTime OPTIONAL,
 endtime                 [7] KerberosTime,
 renew-till              [8] KerberosTime OPTIONAL,
 caddr                   [9] HostAddresses OPTIONAL,
 authorization-data      [10] AuthorizationData OPTIONAL
 }
 */
public class EncTicketPart extends KrbAppSequenceType {
    public static final int TAG = 3;

    FLAGS = 0;
    KEY = 1;
    CREALM = 2;
    CNAME = 3;
    TRANSITED = 4;
    AUTHTIME = 5;
    STARTTIME = 6;
    ENDTIME = 7;
    RENEW_TILL = 8;
    CADDR = 9;
    AUTHORIZATION_DATA = 10;

    static Asn1FieldInfo[] fieldInfos = new Asn1FieldInfo[] {
            new ExplicitField(MyEnum.FLAGS, 0, TicketFlags.class),
            new ExplicitField(MyEnum.KEY, 1, EncryptionKey.class),
            new ExplicitField(MyEnum.CREALM, 2, KerberosString.class),
            new ExplicitField(MyEnum.CNAME, 3, PrincipalName.class),
            new ExplicitField(MyEnum.TRANSITED, 4, TransitedEncoding.class),
            new ExplicitField(MyEnum.AUTHTIME, 5, KerberosTime.class),
            new ExplicitField(MyEnum.STARTTIME, 6, KerberosTime.class),
            new ExplicitField(MyEnum.ENDTIME, 7, KerberosTime.class),
            new ExplicitField(MyEnum.ENDTIME, 8, KerberosTime.class),
            new ExplicitField(MyEnum.CADDR, 9, HostAddresses.class),
            new ExplicitField(MyEnum.AUTHORIZATION_DATA, 10, AuthorizationData.class)
    };

    public EncTicketPart() {
        super(TAG, fieldInfos);
    }

    public TicketFlags getFlags() {
        return getFieldAs(MyEnum.FLAGS, TicketFlags.class);
    }

    public void setFlags(TicketFlags flags) {
        setFieldAs(FLAGS, flags);
    }

    public EncryptionKey getKey() {
        return getFieldAs(MyEnum.KEY, EncryptionKey.class);
    }

    public void setKey(EncryptionKey key) {
        setFieldAs(KEY, key);
    }

    public String getCrealm() {
        return getFieldAsString(CREALM);
    }

    public void setCrealm(String crealm) {
        setFieldAsString(CREALM, crealm);
    }

    public PrincipalName getCname() {
        return getFieldAs(MyEnum.CNAME, PrincipalName.class);
    }

    public void setCname(PrincipalName cname) {
        setFieldAs(CNAME, cname);
    }

    public TransitedEncoding getTransited() {
        return getFieldAs(MyEnum.TRANSITED, TransitedEncoding.class);
    }

    public void setTransited(TransitedEncoding transited) {
        setFieldAs(TRANSITED, transited);
    }

    public KerberosTime getAuthTime() {
        return getFieldAs(MyEnum.AUTHTIME, KerberosTime.class);
    }

    public void setAuthTime(KerberosTime authTime) {
        setFieldAs(AUTHTIME, authTime);
    }

    public KerberosTime getStartTime() {
        return getFieldAs(MyEnum.STARTTIME, KerberosTime.class);
    }

    public void setStartTime(KerberosTime startTime) {
        setFieldAs(STARTTIME, startTime);
    }

    public KerberosTime getEndTime() {
        return getFieldAs(MyEnum.ENDTIME, KerberosTime.class);
    }

    public void setEndTime(KerberosTime endTime) {
        setFieldAs(ENDTIME, endTime);
    }

    public KerberosTime getRenewtill() {
        return getFieldAs(MyEnum.RENEW_TILL, KerberosTime.class);
    }

    public void setRenewtill(KerberosTime renewtill) {
        setFieldAs(RENEW_TILL, renewtill);
    }

    public HostAddresses getClientAddresses() {
        return getFieldAs(MyEnum.CADDR, HostAddresses.class);
    }

    public void setClientAddresses(HostAddresses clientAddresses) {
        setFieldAs(CADDR, clientAddresses);
    }

    public AuthorizationData getAuthorizationData() {
        return getFieldAs(MyEnum.AUTHORIZATION_DATA, AuthorizationData.class);
    }

    public void setAuthorizationData(AuthorizationData authorizationData) {
        setFieldAs(AUTHORIZATION_DATA, authorizationData);
    }
}
