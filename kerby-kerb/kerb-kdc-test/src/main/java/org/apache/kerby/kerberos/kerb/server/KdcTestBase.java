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
import org.apache.kerby.kerberos.kerb.client.KrbClient;
import org.apache.kerby.kerberos.kerb.client.KrbConfig;
import org.apache.kerby.kerberos.kerb.client.KrbConfigKey;
import org.apache.kerby.util.IOUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

public abstract class KdcTestBase {
    protected static final String TEST_PASSWORD = "123456";
    private static File testDir;

    protected String kdcRealm;
    private String clientPrincipal;
    private String serverPrincipal;

    protected String hostname = "localhost";
    protected int tcpPort = -1;
    protected int udpPort = -1;

    protected TestKdcServer kdcServer;
    protected KrbClient krbClnt;

    /**
     * Get a server socket point for testing usage, either TCP or UDP.
     * @return server socket point
     */
    private static int getServerPort() {
        int serverPort = 0;

        try {
            ServerSocket serverSocket = new ServerSocket(0);
            serverPort = serverSocket.getLocalPort();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get a server socket point");
        }

        return serverPort;
    }

    @BeforeClass
    public static void createTestDir() throws IOException {
        String basedir = System.getProperty("basedir");
        if (basedir == null) {
            basedir = new File(".").getCanonicalPath();
        }
        File targetdir= new File(basedir, "target");
        testDir = new File(targetdir, "tmp");
        testDir.mkdirs();
    }

    @AfterClass
    public static void deleteTestDir() throws IOException {
        testDir.delete();
    }

    public File getTestDir() {
        return testDir;
    }

    protected void setClientPrincipal(String clientPrincipal) {
        this.clientPrincipal = clientPrincipal;
    }

    protected String getClientPrincipal() {
        return this.clientPrincipal;
    }

    protected void setServerPrincipal(String serverPrincipal) {
        this.serverPrincipal = serverPrincipal;
    }

    protected String getServerPrincipal() {
        return this.serverPrincipal;
    }

    protected boolean allowUdp() {
        return true;
    }

    protected boolean allowTcp() {
        return true;
    }

    protected String getFileContent(String path) throws IOException {
        return IOUtil.readFile(new File(path));
    }

    protected String writeToTestDir(String content, String fileName) throws IOException {
        File file = new File(testDir, fileName);
        if (file.exists()) {
            file.delete();
        }
        IOUtil.writeFile(content, file);
        return file.getPath();
    }

    @Before
    public void setUp() throws Exception {
        if (allowTcp()) {
            tcpPort = getServerPort();
        }

        if (allowUdp()) {
            udpPort = getServerPort();
        }

        setUpKdcServer();

        setUpClient();
        createPrincipals();
    }

    /**
     * Prepare KrbClient startup options and config.
     * @throws Exception
     */
    protected void prepareKrbClient() throws Exception {

    }

    /**
     * Prepare KDC startup options and config.
     * @throws Exception
     */
    protected void prepareKdcServer() throws Exception {
        kdcServer.setKdcHost(hostname);
        kdcServer.setAllowTcp(allowTcp());
        if (tcpPort > 0) {
            kdcServer.setKdcTcpPort(tcpPort);
        }

        kdcServer.setAllowUdp(allowUdp());
        if (udpPort > 0) {
            kdcServer.setKdcUdpPort(udpPort);
        }
    }

    protected void setUpKdcServer() throws Exception {
        kdcServer = new TestKdcServer();
        prepareKdcServer();
        kdcServer.init();

        kdcRealm = kdcServer.getKdcRealm();
        clientPrincipal = "drankye@" + kdcRealm;
        serverPrincipal = "test-service/localhost@" + kdcRealm;
    }

    protected void setUpClient() throws Exception {
        KrbConfig krbConfig = new KrbConfig();
        krbConfig.setString(KrbConfigKey.PERMITTED_ENCTYPES,
            "aes128-cts-hmac-sha1-96 des-cbc-crc des-cbc-md5 des3-cbc-sha1");

        krbClnt = new KrbClient(krbConfig);
        prepareKrbClient();

        krbClnt.setKdcHost(hostname);
        krbClnt.setAllowTcp(allowTcp());
        if (tcpPort > 0) {
            krbClnt.setKdcTcpPort(tcpPort);
        }
        krbClnt.setAllowUdp(allowUdp());
        if (udpPort > 0) {
            krbClnt.setKdcUdpPort(udpPort);
        }

        krbClnt.setTimeout(1000);
        krbClnt.setKdcRealm(kdcServer.getKdcRealm());
    }

    protected void createPrincipals() throws KrbException {
        kdcServer.createTgsPrincipal();
        kdcServer.createPrincipals(serverPrincipal);
    }

    protected void deletePrincipals() throws KrbException {
        kdcServer.deleteTgsPrincipal();
        kdcServer.deletePrincipals(serverPrincipal);
    }

    @After
    public void tearDown() throws Exception {
        deletePrincipals();
        kdcServer.stop();
    }
}