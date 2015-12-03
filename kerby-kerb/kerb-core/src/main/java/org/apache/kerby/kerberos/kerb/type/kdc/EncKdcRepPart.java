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
package org.apache.kerby.kerberos.kerb.type.kdc;

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.type.Asn1Integer;
import org.apache.kerby.asn1.ExplicitField;
import org.apache.kerby.kerberos.kerb.type.KerberosString;
import org.apache.kerby.kerberos.kerb.type.KerberosTime;
import org.apache.kerby.kerberos.kerb.type.KrbAppSequenceType;
import org.apache.kerby.kerberos.kerb.type.base.EncryptionKey;
import org.apache.kerby.kerberos.kerb.type.base.HostAddresses;
import org.apache.kerby.kerberos.kerb.type.base.LastReq;
import org.apache.kerby.kerberos.kerb.type.base.PrincipalName;
import org.apache.kerby.kerberos.kerb.type.ticket.TicketFlags;

/**
 EncKDCRepPart   ::= SEQUENCE {
 key             [0] EncryptionKey,
 last-req        [1] LastReq,
 nonce           [2] UInt32,
 key-expiration  [3] KerberosTime OPTIONAL,
 flags           [4] TicketFlags,
 authtime        [5] KerberosTime,
 starttime       [6] KerberosTime OPTIONAL,
 endtime         [7] KerberosTime,
 renew-till      [8] KerberosTime OPTIONAL,
 srealm          [9] Realm,
 sname           [10] PrincipalName,
 caddr           [11] HostAddresses OPTIONAL
 }
 */
public abstract class EncKdcRepPart extends KrbAppSequenceType {
    KEY = 0;
    LAST_REQ = 1;
    NONCE = 2;
    KEY_EXPIRATION = 3;
    FLAGS = 4;
    AUTHTIME = 5;
    STARTTIME = 6;
    ENDTIME = 7;
    RENEW_TILL = 8;
    SREALM = 9;
    SNAME = 10;
    CADDR = 11;

    static Asn1FieldInfo[] fieldInfos = new Asn1FieldInfo[] {
            new ExplicitField(MyEnum.KEY, EncryptionKey.class),
            new ExplicitField(MyEnum.LAST_REQ, LastReq.class),
            new ExplicitField(MyEnum.NONCE, Asn1Integer.class),
            new ExplicitField(MyEnum.KEY_EXPIRATION, KerberosTime.class),
            new ExplicitField(MyEnum.FLAGS, TicketFlags.class),
            new ExplicitField(MyEnum.AUTHTIME, KerberosTime.class),
            new ExplicitField(MyEnum.STARTTIME, KerberosTime.class),
            new ExplicitField(MyEnum.ENDTIME, KerberosTime.class),
            new ExplicitField(MyEnum.RENEW_TILL, KerberosTime.class),
            new ExplicitField(MyEnum.SREALM, KerberosString.class),
            new ExplicitField(MyEnum.SNAME, PrincipalName.class),
            new ExplicitField(MyEnum.CADDR, HostAddresses.class)
    };

    public EncKdcRepPart(int tagNo) {
        super(tagNo, fieldInfos);
    }

    public EncryptionKey getKey() {
        return getFieldAs(MyEnum.KEY, EncryptionKey.class);
    }

    public void setKey(EncryptionKey key) {
        setFieldAs(KEY, key);
    }

    public LastReq getLastReq() {
        return getFieldAs(MyEnum.LAST_REQ, LastReq.class);
    }

    public void setLastReq(LastReq lastReq) {
        setFieldAs(LAST_REQ, lastReq);
    }

    public int getNonce() {
        return getFieldAsInt(NONCE);
    }

    public void setNonce(int nonce) {
        setFieldAsInt(NONCE, nonce);
    }

    public KerberosTime getKeyExpiration() {
        return getFieldAsTime(KEY_EXPIRATION);
    }

    public void setKeyExpiration(KerberosTime keyExpiration) {
        setFieldAs(KEY_EXPIRATION, keyExpiration);
    }

    public TicketFlags getFlags() {
        return getFieldAs(MyEnum.FLAGS, TicketFlags.class);
    }

    public void setFlags(TicketFlags flags) {
        setFieldAs(FLAGS, flags);
    }

    public KerberosTime getAuthTime() {
        return getFieldAsTime(AUTHTIME);
    }

    public void setAuthTime(KerberosTime authTime) {
        setFieldAs(AUTHTIME, authTime);
    }

    public KerberosTime getStartTime() {
        return getFieldAsTime(STARTTIME);
    }

    public void setStartTime(KerberosTime startTime) {
        setFieldAs(STARTTIME, startTime);
    }

    public KerberosTime getEndTime() {
        return getFieldAsTime(ENDTIME);
    }

    public void setEndTime(KerberosTime endTime) {
        setFieldAs(ENDTIME, endTime);
    }

    public KerberosTime getRenewTill() {
        return getFieldAsTime(RENEW_TILL);
    }

    public void setRenewTill(KerberosTime renewTill) {
        setFieldAs(RENEW_TILL, renewTill);
    }

    public String getSrealm() {
        return getFieldAsString(SREALM);
    }

    public void setSrealm(String srealm) {
        setFieldAsString(SREALM, srealm);
    }

    public PrincipalName getSname() {
        return getFieldAs(MyEnum.SNAME, PrincipalName.class);
    }

    public void setSname(PrincipalName sname) {
        setFieldAs(SNAME, sname);
    }

    public HostAddresses getCaddr() {
        return getFieldAs(MyEnum.CADDR, HostAddresses.class);
    }

    public void setCaddr(HostAddresses caddr) {
        setFieldAs(CADDR, caddr);
    }
}
