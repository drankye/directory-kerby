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
import org.apache.kerby.asn1.type.Asn1Integer;
import org.apache.kerby.asn1.type.Asn1SequenceType;
import org.apache.kerby.asn1.ExplicitField;

/**
 *
 * Ref. RFC 3280.
 * <pre>
 *       GeneralSubtree ::= SEQUENCE {
 *         base                    GeneralName,
 *         minimum         [0]     BaseDistance DEFAULT 0,
 *         maximum         [1]     BaseDistance OPTIONAL 
 *       }
 * </pre>
 * 
 */
public class GeneralSubtree extends Asn1SequenceType {
    BASE = 0;
    MINIMUM = 1;
    MAXMUM = 2;

    static Asn1FieldInfo[] fieldInfos = new Asn1FieldInfo[] {
        new Asn1FieldInfo(BASE, GeneralName.class),
        new ExplicitField(MyEnum.MINIMUM, 0, Asn1Integer.class),
        new ExplicitField(MyEnum.MAXMUM, 1, Asn1Integer.class)
    };

    public GeneralSubtree() {
        super(fieldInfos);
    }

    public GeneralName getBase() {
        return getFieldAs(MyEnum.BASE, GeneralName.class);
    }

    public void setBase(GeneralName base) {
        setFieldAs(BASE, base);
    }

    public int getMinimum() {
        return getFieldAsInteger(MINIMUM);
    }

    public void setMinimum(int minimum) {
        setFieldAsInt(MINIMUM, minimum);
    }

    public int getMaximum() {
        return getFieldAsInteger(MAXMUM);
    }

    public void setMaxmum(int maxmum) {
        setFieldAsInt(MAXMUM, maxmum);
    }
}
