package org.apache.kerby.kerberos.kerb.server;

import org.apache.kerby.util.IOUtil;

import java.io.File;
import java.io.IOException;

/**
 * Generate krb5 file using given kdc server settings.
 */
public class Krb5Conf {
    private static final String KRB5_CONF = "java.security.krb5.conf";
    private static final String KRB5_CONF_FILE = "krb5.conf";
    private SimpleKdc kdcServer;
    private int udpLimit = 4096;

    public Krb5Conf(SimpleKdc kdcServer) {
        this.kdcServer = kdcServer;
    }

    public void setUdpLimit(int udpLimit) {
        this.udpLimit = udpLimit;
    }

    public void initKrb5conf() throws IOException {
        File confFile = generateConfFile();
        System.setProperty(KRB5_CONF, confFile.getAbsolutePath());
    }

    // Read in krb5.conf and substitute in the correct port
    private File generateConfFile() throws IOException {
        String resourcePath = "/" + KRB5_CONF_FILE;
        String templateResource = getClass().getResource(resourcePath).getPath();
        File templateFile = new File(templateResource);
        String templateContent = IOUtil.readFile(templateFile);

        String content = templateContent;

        content = content.replaceAll("_REALM_", "" + kdcServer.getKdcRealm());

        content = content.replaceAll("_PORT_",
                String.valueOf(kdcServer.getKdcHost()));

        content = content.replaceAll("_UDP_LIMIT_", String.valueOf(udpLimit));

        File confFile = new File(kdcServer.getWorkDir(), KRB5_CONF_FILE);
        IOUtil.writeFile(content, confFile);

        return confFile;
    }
}
