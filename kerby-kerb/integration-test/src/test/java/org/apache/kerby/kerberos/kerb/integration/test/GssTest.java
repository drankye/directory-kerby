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
package org.apache.kerby.kerberos.kerb.integration.test;

import org.apache.kerby.kerberos.kerb.integration.test.gss.GssAppClient;
import org.apache.kerby.kerberos.kerb.integration.test.gss.GssAppServer;

import java.io.IOException;

/**
 * This is an interop test using the Java GSS APIs against the Kerby KDC
 */
public class GssTest extends IntegrationTest {

    @Override
    protected AppServer createAppServer() throws Exception {
        return new GssAppServer(null);
    }

    @Override
    protected AppClient createAppClient() throws Exception {
        return new GssAppClient(null);
    }
}