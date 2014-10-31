package org.haox.kerb.server;

import org.haox.kerb.spec.KrbException;
import org.haox.kerb.spec.type.ticket.ServiceTicket;
import org.haox.kerb.spec.type.ticket.TgtTicket;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;

/**
 openssl genrsa -out cakey.pem 2048
 openssl req -key cakey.pem -new -x509 -out cacert.pem -days 3650
 vi extensions.kdc
 openssl genrsa -out kdckey.pem 2048
 openssl req -new -out kdc.req -key kdckey.pem
 env REALM=SH.INTEL.COM openssl x509 -req -in kdc.req -CAkey cakey.pem \
 -CA cacert.pem -out kdc.pem -days 365 -extfile extensions.kdc -extensions kdc_cert -CAcreateserial
 */
public class WithCertKdcTest extends KdcTestBase {

    private Certificate cert;

    @Override
    protected void setUpKdcServer() {
        super.setUpKdcServer();
        kdcServer.createPrincipals(clientPrincipal);
    }

    @Test
    public void testKdc() throws Exception {
        kdcServer.start();
        Assert.assertTrue(kdcServer.isStarted());
        krbClnt.init();

        TgtTicket tgt = null;
        try {
            tgt = krbClnt.requestTgtTicket(clientPrincipal, cert);
        } catch (KrbException te) {
            Assert.assertTrue(te.getMessage().contains("timeout"));
            return;
        }
        Assert.assertNull(tgt);

        ServiceTicket tkt = krbClnt.requestServiceTicket(tgt, serverPrincipal);
        Assert.assertNull(tkt);
    }

    private void loadCert() {
        //CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        //Collection<? extends Certificate> certs =
        //        (Collection<? extends Certificate>) certFactory.generateCertificates(new ByteArrayInputStream(FileUtils.readFileToByteArray(serverCertFile)));
    }
}