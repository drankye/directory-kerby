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
package org.apache.kerby.kerberos.kerb.server;

import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.admin.Kadmin;
import org.apache.kerby.kerberos.kerb.client.KrbClient;
import org.apache.kerby.util.NetworkUtil;

import java.io.File;
import java.io.IOException;

/**
 * A simple KDC server mainly for test usage.
 */
public class SimpleKdcServer {
    private final KdcServer kdcServer;
    private final KrbClient krbClnt;
    private Kadmin kadmin;

    private File workDir;

    public SimpleKdcServer() {
        this.kdcServer = new KdcServer();

        this.krbClnt = new KrbClient();
    }

    public void setWorkDir(File workDir) {
        this.workDir = workDir;
    }

    public File getWorkDir() {
        return workDir;
    }

    public void init() throws KrbException {
        prepareKdcServer();

        kdcServer.init();

        kadmin = new Kadmin(kdcServer.getSetting(),
                kdcServer.getIdentityService());

        kadmin.createBuiltinPrincipals();

        try {
            Krb5Conf krb5Conf = new Krb5Conf(this);
            krb5Conf.initKrb5conf();
        } catch (IOException e) {
            throw new KrbException("Failed to make krb5.conf", e);
        }
    }

    /**
     * Prepare Kdc server side startup options and config.
     * @throws Exception
     */
    protected void prepareKdcServer() throws KrbException {
        KdcConfig kdcConfig = kdcServer.getKdcConfig();
        kdcConfig.setString(KdcConfigKey.KDC_HOST, "localhost");
        kdcConfig.setInt(KdcConfigKey.KDC_PORT, NetworkUtil.getServerPort());
        kdcConfig.setString(KdcConfigKey.KDC_REALM, "EXAMPLE.COM");
    }

    /**
     * Prepare KrbClient startup options and config.
     * @throws Exception
     */
    protected void prepareKrbClient() throws Exception {

    }

    public KdcServer getKdcServer() {
        return kdcServer;
    }

    public KdcSetting getKdcSetting() {
        return kdcServer.getSetting();
    }

    public KrbClient getKrbClient() {
        return krbClnt;
    }

    /**
     * Get Kadmin operation interface.
     * @return
     */
    public Kadmin getKadmin() {
        return kadmin;
    }

    public String getKdcRealm() {
        return kdcServer.getSetting().getKdcRealm();
    }

    public void createPrincipal(String principal) throws KrbException {
        kadmin.addPrincipal(principal);
    }

    public void createPrincipal(String principal,
                                String password) throws KrbException {
        kadmin.addPrincipal(principal, password);
    }

    public void createPrincipals(String ... principals) throws KrbException {
        for (String principal : principals) {
            kadmin.addPrincipal(principal);
        }
    }

    /**
     * Creates principals and export their keys to the specified keytab file.
     */
    public void createAndExportPrincipals(File keytabFile,
                                String ... principals) throws KrbException {
        createPrincipals(principals);
        exportPrincipals(keytabFile);
    }

    public void deletePrincipals(String ... principals) throws KrbException {
        for (String principal : principals) {
            deletePrincipal(principal);
        }
    }

    public void deletePrincipal(String principal) throws KrbException {
        kadmin.deletePrincipal(principal);
    }

    public void exportPrincipals(File keytabFile) throws KrbException {
        kadmin.exportKeytab(keytabFile);
    }

    public void start() throws KrbException {
        kdcServer.start();
    }

    public void stop() throws KrbException {
        kdcServer.stop();
    }
}
