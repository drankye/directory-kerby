package org.apache.kerby.kerberos.kerb.integration.test.gss;

import org.apache.kerby.kerberos.kerb.integration.test.AppServer;
import org.apache.kerby.kerberos.kerb.integration.test.AppUtil;
import org.apache.kerby.kerberos.kerb.integration.test.Transport;
import org.ietf.jgss.*;

import java.io.IOException;

public class GssAppServer extends AppServer {
    private String serverPrincipal;
    private GSSManager manager;

    public GssAppServer(String[] args) throws IOException {
        super(args);
        if (args.length < 2) {
            usage(args);
        }
        this.serverPrincipal = args[1];
        this.manager = GSSManager.getInstance();
    }

    protected void usage(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: AppServer <ListenPort> <server-principal>");
            System.exit(-1);
        }
    }

    @Override
    protected void onConnection(Transport.Connection conn) throws Exception {
        GSSName gssService = manager.createName(serverPrincipal,
                GSSName.NT_USER_NAME);

        Oid oid = new Oid(AppUtil.JGSS_KERBEROS_OID);
        GSSCredential credentials = manager.createCredential(gssService,
                GSSCredential.DEFAULT_LIFETIME, oid, GSSCredential.ACCEPT_ONLY);
        GSSContext context = manager.createContext(credentials);

        byte[] token;

        System.out.print("Starting negotiating security context");
        while (!context.isEstablished()) {
            token = conn.recvToken();
            token = context.acceptSecContext(token, 0, token.length);
            if (token != null) {
                conn.sendToken(token);
            }
        }

        System.out.print("Context Established! ");
        System.out.println("Client is " + context.getSrcName());
        System.out.println("Server is " + context.getTargName());

        doWith(context, conn);

        context.dispose();
    }

    protected void doWith(GSSContext context, Transport.Connection conn) throws Exception {
        byte[] token = null;

        if (context.getMutualAuthState())
            System.out.println("Mutual authentication took place!");

        MessageProp prop = new MessageProp(0, false);

        token = conn.recvToken();
        byte[] bytes = context.unwrap(token, 0, token.length, prop);
        String str = new String(bytes);
        System.out.println("Received data \""
                + str + "\" of length " + str.length());

        System.out.println("Confidentiality applied: "
                + prop.getPrivacy());

        prop.setQOP(0);
        token = context.getMIC(bytes, 0, bytes.length, prop);
        System.out.println("Will send MIC token of size "
                + token.length);
        conn.sendToken(token);
    }

    public static void main(String[] args) throws Exception {
        new GssAppServer(args).run();
    }
}
