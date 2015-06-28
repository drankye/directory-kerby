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
import org.apache.kerby.kerberos.kerb.common.KrbUtil;
import org.apache.kerby.util.NetworkUtil;

import java.io.File;
import java.io.IOException;

/**
 * A simple KDC server mainly for test usage.
 */
public class SimpleKdc {
    private KdcServer kdcServer;
    private KrbClient krbClnt;
    private Kadmin kadmin;

    private File workDir;

    public SimpleKdc(File workDir) {
        this.workDir = workDir;
        this.kdcServer = new KdcServer();
        this.krbClnt = new KrbClient();
    }

    public File getWorkDir() {
        return workDir;
    }

    /**
     * Prepare KDC configuration.
     */
    protected void prepareKdcConfig() {
        KdcConfig kdcConfig = kdcServer.getKdcConfig();
        kdcConfig.setString(KdcConfigKey.KDC_HOST, "localhost");
        kdcConfig.setInt(KdcConfigKey.KDC_PORT, NetworkUtil.getServerPort());
        kdcConfig.setString(KdcConfigKey.KDC_REALM, "EXAMPLE.COM");
    }

    /**
     * Prepare KDC startup options and config.
     */
    protected void prepareKdcServer() throws Exception {

    }

    public void init() throws KrbException {
        kdcServer.init();

        prepareKdcConfig();

        kadmin = new Kadmin(kdcServer.getIdentityService(),
                kdcServer.getKdcConfig(), kdcServer.getBackendConfig());

        try {
            Krb5Conf krb5Conf = new Krb5Conf(this);
            krb5Conf.initKrb5conf();
        } catch (IOException e) {
            throw new KrbException("Failed to make krb5.conf", e);
        }
    }

    public KdcServer getKdcServer() {
        return kdcServer;
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

    private String getTgsPrincipal() {
        return KrbUtil.makeTgsPrincipal(getKdcRealm()).getName();
    }

    public void createTgsPrincipal() throws KrbException {
        createPrincipal(getTgsPrincipal());
    }

    public void deleteTgsPrincipal() throws KrbException {
        deletePrincipal(getTgsPrincipal());
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

    public void start() {
        kdcServer.start();
    }

    public void stop() {
        kdcServer.stop();
    }
}
