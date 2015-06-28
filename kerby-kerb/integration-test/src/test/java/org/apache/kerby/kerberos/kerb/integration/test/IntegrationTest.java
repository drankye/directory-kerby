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

import org.apache.kerby.kerberos.kerb.server.KdcTestBase;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.security.Principal;
import java.security.PrivilegedExceptionAction;
import java.util.HashSet;
import java.util.Set;

public abstract class IntegrationTest extends KdcTestBase {
    protected File clientCredentialFile;
    protected File serviceKeytabFile;
    protected AppClient appClient;
    protected AppServer appServer;

    protected void setupAppServer() throws Exception {
        appServer = createAppServer();
        new Thread(appServer).start();
    }

    protected abstract AppServer createAppServer() throws Exception;

    protected void setupAppClient() throws Exception {
        appClient = createAppClient();
        new Thread(appClient).start();
    }

    protected abstract AppClient createAppClient() throws Exception;


    protected <T> T loginAndDoAs(boolean isClient,
                                 final PrivilegedExceptionAction<T> action)
            throws Exception {
        LoginContext loginContext = createLoginContext(isClient);
        try {
            loginContext.login();
            Subject subject = loginContext.getSubject();
            return Subject.doAs(subject, action);
        } finally {
            loginContext.logout();
        }
    }

    private LoginContext createLoginContext(boolean isClient) throws LoginException {
        String principal = isClient ? getClientPrincipalName() :
                getServerPrincipalName();
        Set<Principal> principals = new HashSet<Principal>();
        principals.add(new KerberosPrincipal(principal));

        Subject subject = new Subject(false, principals,
                new HashSet<Object>(), new HashSet<Object>());

        Configuration conf = isClient ? JaasKrbConf.createClientConfig(principal,
                clientCredentialFile) :
                JaasKrbConf.createServerConfig(principal, serviceKeytabFile);
        String confName = isClient ? "clientConf" : "serverConf";
        LoginContext loginContext = new LoginContext(confName, subject, null, conf);
        return loginContext;
    }
}