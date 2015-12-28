package org.apache.kerby.util;

import javax.crypto.Cipher;

/**
 * This class gives a method to detect if system support AES256 or above.
 */
public class EncryptoUtil {
    private static boolean isAES256Enabled = false;

    static {
        try {
            isAES256Enabled = Cipher.getMaxAllowedKeyLength("AES") >= 256;
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * @return true if aes256 is enabled
     */
    public static boolean isAES256Enabled() {
        return isAES256Enabled;
    }

}
