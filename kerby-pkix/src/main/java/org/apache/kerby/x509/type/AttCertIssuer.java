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
import org.apache.kerby.asn1.EnumType;
import org.apache.kerby.asn1.ExplicitField;
import org.apache.kerby.asn1.type.Asn1Choice;

import static org.apache.kerby.x509.type.AttCertIssuer.MyEnum.V1_FORM;
import static org.apache.kerby.x509.type.AttCertIssuer.MyEnum.V2_FORM;

/**
 *
 * <pre>
 *  AttCertIssuer ::= CHOICE {
 *       v1Form   GeneralNames,  -- MUST NOT be used in this profile
 *       v2Form   [0] V2Form     -- v2 only
 *  }
 * </pre>
 */
public class AttCertIssuer extends Asn1Choice {
    protected enum MyEnum implements EnumType {
        V1_FORM,
        V2_FORM;

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
        new Asn1FieldInfo(V1_FORM, GeneralNames.class),
        new ExplicitField(V2_FORM, 0, V2Form.class)
    };

    public AttCertIssuer() {
        super(fieldInfos);
    }

    public GeneralNames getV1Form() {
        return getChoiceValueAs(V1_FORM, GeneralNames.class);
    }

    public void setV1Form(GeneralNames v1Form) {
        setChoiceValue(V1_FORM, v1Form);
    }

    public V2Form getV2Form() {
        return getChoiceValueAs(V2_FORM, V2Form.class);
    }

    public void setV2Form(V2Form v2Form) {
        setChoiceValue(V2_FORM, v2Form);
    }
}
