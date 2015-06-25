package org.apache.kerby.kerberos.kerb.integration.test;

public abstract class TestApp implements Runnable {
    private boolean isTestOK = false;

    public synchronized void setTestOK(boolean isOK) {
        this.isTestOK = isOK;
    }

    public boolean isTestOK() {
        return isTestOK;
    }
}
