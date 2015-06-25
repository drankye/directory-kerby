package org.apache.kerby.kerberos.kerb.integration.test;

import java.io.IOException;

public abstract class AppClient extends TestApp {
    protected Transport.Connection conn;

    protected void usage(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java <options> AppClient "
                    + "<server-host> <server-port>");
            System.exit(-1);
        }
    }

    public AppClient(String[] args) throws Exception {
        usage(args);

        String hostName = args[0];
        short port = (short) Integer.parseInt(args[1]);

        this.conn = Transport.Connector.connect(hostName, port);
    }

    public void run() {
        System.out.println("Connected to server");

        try {
            withConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void withConnection(Transport.Connection conn) throws Exception;
}
