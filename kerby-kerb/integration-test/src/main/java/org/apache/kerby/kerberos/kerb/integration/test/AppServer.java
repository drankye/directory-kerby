package org.apache.kerby.kerberos.kerb.integration.test;

import java.io.IOException;

public abstract class AppServer extends TestApp {
    protected Transport.Acceptor acceptor;
    private boolean terminated = false;

    protected void usage(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java <options> AppServer <ListenPort>");
            System.exit(-1);
        }
    }

    public AppServer(String[] args) throws IOException {
        usage(args);

        short listenPort = (short) Integer.parseInt(args[0]);
        this.acceptor = new Transport.Acceptor(listenPort);
    }

    public synchronized void terminate() {
        terminated = true;
    }

    public void run() {
        try {
            synchronized (this) {
                while (!terminated) {
                    runOnce();
                }
            }
        } finally {
            acceptor.close();
        }
    }

    private void runOnce() {
        System.out.println("Waiting for incoming connection...");

        Transport.Connection conn = acceptor.accept();
        System.out.println("Got connection from client");

        try {
            onConnection(conn);
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

    protected abstract void onConnection(Transport.Connection conn) throws Exception;
}
