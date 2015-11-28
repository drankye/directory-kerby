package org.apache.kerby.cms;

import org.apache.kerby.cms.type.ContentInfo;
import org.apache.kerby.cms.type.SignedData;
import org.apache.kerby.util.Base64;

import java.io.IOException;

public class SignedTest {
    Base64 base64 = new Base64();
    byte[]   signedData = base64.decode(
        "MIAGCSqGSIb3DQEHAqCAMIACAQExCzAJBgUrDgMCGgUAMIAGCSqGSIb3DQEHAaCA"
            + "JIAEDEhlbGxvIFdvcmxkIQAAAAAAAKCCBGIwggINMIIBdqADAgECAgEBMA0GCSqG"
            + "SIb3DQEBBAUAMCUxFjAUBgNVBAoTDUJvdW5jeSBDYXN0bGUxCzAJBgNVBAYTAkFV"
            + "MB4XDTA0MTAyNDA0MzA1OFoXDTA1MDIwMTA0MzA1OFowJTEWMBQGA1UEChMNQm91"
            + "bmN5IENhc3RsZTELMAkGA1UEBhMCQVUwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJ"
            + "AoGBAJj3OAshAOgDmPcYZ1jdNSuhOHRH9VhC/PG17FdiInVGc2ulJhEifEQga/uq"
            + "ZCpSd1nHsJUZKm9k1bVneWzC0941i9Znfxgb2jnXXsa5kwB2KEVESrOWsRjSRtnY"
            + "iLgqBG0rzpaMn5A5ntu7N0406EesBhe19cjZAageEHGZDbufAgMBAAGjTTBLMB0G"
            + "A1UdDgQWBBR/iHNKOo6f4ByWFFywRNZ65XSr1jAfBgNVHSMEGDAWgBR/iHNKOo6f"
            + "4ByWFFywRNZ65XSr1jAJBgNVHRMEAjAAMA0GCSqGSIb3DQEBBAUAA4GBAFMJJ7QO"
            + "pHo30bnlQ4Ny3PCnK+Se+Gw3TpaYGp84+a8fGD9Dme78G6NEsgvpFGTyoLxvJ4CB"
            + "84Kzys+1p2HdXzoZiyXAer5S4IwptE3TxxFwKyj28cRrM6dK47DDyXUkV0qwBAMN"
            + "luwnk/no4K7ilzN2MZk5l7wXyNa9yJ6CHW6dMIICTTCCAbagAwIBAgIBAjANBgkq"
            + "hkiG9w0BAQQFADAlMRYwFAYDVQQKEw1Cb3VuY3kgQ2FzdGxlMQswCQYDVQQGEwJB"
            + "VTAeFw0wNDEwMjQwNDMwNTlaFw0wNTAyMDEwNDMwNTlaMGUxGDAWBgNVBAMTD0Vy"
            + "aWMgSC4gRWNoaWRuYTEkMCIGCSqGSIb3DQEJARYVZXJpY0Bib3VuY3ljYXN0bGUu"
            + "b3JnMRYwFAYDVQQKEw1Cb3VuY3kgQ2FzdGxlMQswCQYDVQQGEwJBVTCBnzANBgkq"
            + "hkiG9w0BAQEFAAOBjQAwgYkCgYEAm+5CnGU6W45iUpCsaGkn5gDruZv3j/o7N6ag"
            + "mRZhikaLG2JF6ECaX13iioVJfmzBsPKxAACWwuTXCoSSXG8viK/qpSHwJpfQHYEh"
            + "tcC0CxIqlnltv3KQAGwh/PdwpSPvSNnkQBGvtFq++9gnXDBbynfP8b2L2Eis0X9U"
            + "2y6gFiMCAwEAAaNNMEswHQYDVR0OBBYEFEAmOksnF66FoQm6IQBVN66vJo1TMB8G"
            + "A1UdIwQYMBaAFH+Ic0o6jp/gHJYUXLBE1nrldKvWMAkGA1UdEwQCMAAwDQYJKoZI"
            + "hvcNAQEEBQADgYEAEeIjvNkKMPU/ZYCu1TqjGZPEqi+glntg2hC/CF0oGyHFpMuG"
            + "tMepF3puW+uzKM1s61ar3ahidp3XFhr/GEU/XxK24AolI3yFgxP8PRgUWmQizTQX"
            + "pWUmhlsBe1uIKVEfNAzCgtYfJQ8HJIKsUCcdWeCKVKs4jRionsek1rozkPExggEv"
            + "MIIBKwIBATAqMCUxFjAUBgNVBAoTDUJvdW5jeSBDYXN0bGUxCzAJBgNVBAYTAkFV"
            + "AgECMAkGBSsOAwIaBQCgXTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqG"
            + "SIb3DQEJBTEPFw0wNDEwMjQwNDMwNTlaMCMGCSqGSIb3DQEJBDEWBBQu973mCM5U"
            + "BOl9XwQvlfifHCMocTANBgkqhkiG9w0BAQEFAASBgGHbe3/jcZu6b/erRhc3PEji"
            + "MUO8mEIRiNYBr5/vFNhkry8TrGfOpI45m7gu1MS0/vdas7ykvidl/sNZfO0GphEI"
            + "UaIjMRT3U6yuTWF4aLpatJbbRsIepJO/B2kdIAbV5SCbZgVDJIPOR2qnruHN2wLF"
            + "a+fEv4J8wQ8Xwvk0C8iMAAAAAAAA");


    private void signedTest() throws IOException {
        ContentInfo contentInfo = new ContentInfo();
        contentInfo.decode(signedData);
        SignedData sData = (SignedData) contentInfo.getContent();
    }

    public void main(String[] args) throws IOException {
        signedTest();
    }
}
