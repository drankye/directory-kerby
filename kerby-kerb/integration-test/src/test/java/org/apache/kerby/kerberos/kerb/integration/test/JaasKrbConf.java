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

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class JaasKrbConf {

    public static Configuration createClientConfig(
            String principal, File credentialFile) {
        return new ClientJaasKrbConf(principal, credentialFile);
    }

    public static Configuration createServerConfig(
            String principal, File keytabFile) {
        return new ServerJaasKrbConf(principal, keytabFile);
    }

    private static String getKrb5LoginModuleName() {
        return System.getProperty("java.vendor").contains("IBM")
                ? "com.ibm.security.auth.module.Krb5LoginModule"
                : "com.sun.security.auth.module.Krb5LoginModule";
    }

    static class ServerJaasKrbConf extends Configuration {
        private String principal;
        private File keytabFile;

        public ServerJaasKrbConf(String principal, File keytab) {
            this.principal = principal;
            this.keytabFile = keytab;
        }

        @Override
        public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
            Map<String, String> options = new HashMap<String, String>();
            options.put("keyTab", keytabFile.getAbsolutePath());
            options.put("principal", principal);
            options.put("useKeyTab", "true");
            options.put("storeKey", "true");
            options.put("doNotPrompt", "true");
            options.put("renewTGT", "true");
            options.put("refreshKrb5Config", "true");
            options.put("isInitiator", Boolean.toString(false));
            options.put("debug", "true");

            return new AppConfigurationEntry[]{
                    new AppConfigurationEntry(getKrb5LoginModuleName(),
                            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                            options)};
        }
    }

    static class ClientJaasKrbConf extends Configuration {
        private String principal;
        private File clientCredentialFile;

        public ClientJaasKrbConf(String principal, File clientCredentialFile) {
            this.principal = principal;
            this.clientCredentialFile = clientCredentialFile;
        }

        @Override
        public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
            Map<String, String> options = new HashMap<String, String>();
            options.put("principal", principal);
            options.put("storeKey", "true");
            options.put("doNotPrompt", "true");
            options.put("useTicketCache", "true");
            options.put("renewTGT", "true");
            options.put("refreshKrb5Config", "true");
            options.put("isInitiator", Boolean.toString(true));
            options.put("ticketCache", clientCredentialFile.getAbsolutePath());
            options.put("debug", "true");

            return new AppConfigurationEntry[]{
                    new AppConfigurationEntry(getKrb5LoginModuleName(),
                            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                            options)};
        }
    }
}