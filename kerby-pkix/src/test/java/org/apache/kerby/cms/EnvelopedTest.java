package org.apache.kerby.cms;

import org.apache.kerby.util.Base64;

public class EnvelopedTest {
    Base64 base64 = new Base64();
    byte[]   envDataKeyTrns = base64.decode(
        "MIAGCSqGSIb3DQEHA6CAMIACAQAxgcQwgcECAQAwKjAlMRYwFAYDVQQKEw1Cb3Vu"
            + "Y3kgQ2FzdGxlMQswCQYDVQQGEwJBVQIBCjANBgkqhkiG9w0BAQEFAASBgC5vdGrB"
            + "itQSGwifLf3KwPILjaB4WEXgT/IIO1KDzrsbItCJsMA0Smq2y0zptxT0pSRL6JRg"
            + "NMxLk1ySnrIrvGiEPLMR1zjxlT8yQ6VLX+kEoK43ztd1aaLw0oBfrcXcLN7BEpZ1"
            + "TIdjlBfXIOx1S88WY1MiYqJJFc3LMwRUaTEDMIAGCSqGSIb3DQEHATAdBglghkgB"
            + "ZQMEARYEEAfxLMWeaBOTTZQwUq0Y5FuggAQgwOJhL04rjSZCBCSOv5i5XpFfGsOd"
            + "YSHSqwntGpFqCx4AAAAAAAAAAAAA");

    byte[]   envDataKEK = base64.decode(
        "MIAGCSqGSIb3DQEHA6CAMIACAQIxUqJQAgEEMAcEBQECAwQFMBAGCyqGSIb3DQEJE"
            + "AMHAgE6BDC7G/HyUPilIrin2Yeajqmj795VoLWETRnZAAFcAiQdoQWyz+oCh6WY/H"
            + "jHHi+0y+cwgAYJKoZIhvcNAQcBMBQGCCqGSIb3DQMHBAiY3eDBBbF6naCABBiNdzJb"
            + "/v6+UZB3XXKipxFDUpz9GyjzB+gAAAAAAAAAAAAA");

    byte[] envDataNestedNDEF = base64.decode(
        "MIAGCSqGSIb3DQEHA6CAMIACAQAxge8wgewCAQAwgZUwgY8xKDAmBgNVBAoMH1RoZSBMZWdpb24g"
            + "b2YgdGhlIEJvdW5jeSBDYXN0bGUxLzAtBgkqhkiG9w0BCQEWIGZlZWRiYWNrLWNyeXB0b0Bib3Vu"
            + "Y3ljYXN0bGUub3JnMREwDwYDVQQIDAhWaWN0b3JpYTESMBAGA1UEBwwJTWVsYm91cm5lMQswCQYD"
            + "VQQGEwJBVQIBATANBgkqhkiG9w0BAQEFAARABIXMd8xiTyWDKO/LQfvdGYTPW3I9oSQWwtm4OIaN"
            + "VINpfY2lfwTvbmE6VXiLKeALC0dMBV8z7DEM9hE0HVmvLDCABgkqhkiG9w0BBwEwHQYJYIZIAWUD"
            + "BAECBBB32ko6WrVxDTqwUYEpV6IUoIAEggKgS6RowrhNlmWWI13zxD/lryxkZ5oWXPUfNiUxYX/P"
            + "r5iscW3s8VKJKUpJ4W5SNA7JGL4l/5LmSnJ4Qu/xzxcoH4r4vmt75EDE9p2Ob2Xi1NuSFAZubJFc"
            + "Zlnp4e05UHKikmoaz0PbiAi277sLQlK2FcVsntTYVT00y8+IwuuQu0ATVqkXC+VhfjV/sK6vQZnw"
            + "2rQKedZhLB7B4dUkmxCujb/UAq4lgSpLMXg2P6wMimTczXyQxRiZxPeI4ByCENjkafXbfcJft2eD"
            + "gv1DEDdYM5WrW9Z75b4lmJiOJ/xxDniHCvum7KGXzpK1d1mqTlpzPC2xoz08/MO4lRf5Mb0bYdq6"
            + "CjMaYqVwGsYryp/2ayX+d8H+JphEG+V9Eg8uPcDoibwhDI4KkoyGHstPw5bxcy7vVFt7LXUdNjJc"
            + "K1wxaUKEXDGKt9Vj93FnBTLMX0Pc9HpueV5o1ipX34dn/P3HZB9XK8ScbrE38B1VnIgylStnhVFO"
            + "Cj9s7qSVqI2L+xYHJRHsxaMumIRnmRuOqdXDfIo28EZAnFtQ/b9BziMGVvAW5+A8h8s2oazhSmK2"
            + "23ftV7uv98ScgE8fCd3PwT1kKJM83ThTYyBzokvMfPYCCvsonMV+kTWXhWcwjYTS4ukrpR452ZdW"
            + "l3aJqDnzobt5FK4T8OGciOj+1PxYFZyRmCuafm2Dx6o7Et2Tu/T5HYvhdY9jHyqtDl2PXH4CTnVi"
            + "gA1YOAArjPVmsZVwAM3Ml46uyXXhcsXwQ1X0Tv4D+PSa/id4UQ2cObOw8Cj1eW2GB8iJIZVqkZaU"
            + "XBexqgWYOIoxjqODSeoZKiBsTK3c+oOUBqBDueY1i55swE2o6dDt95FluX6iyr/q4w2wLt3upY1J"
            + "YL+TuvZxAKviuAczMS1bAAAAAAAAAAAAAA==");



    private static void envelopedTest() {
        //
        // Key trans
        //
        /*
        ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(envDataKeyTrns));

        ContentInfo     info = ContentInfo.getInstance(aIn.readObject());
        EnvelopedData   envData = EnvelopedData.getInstance(info.getContent());
        ASN1Set         s = envData.getRecipientInfos();

        RecipientInfo   recip = RecipientInfo.getInstance(s.getObjectAt(0));

        KeyTransRecipientInfo   inf = KeyTransRecipientInfo.getInstance(recip.getInfo());


        //
        // KEK
        //
        aIn = new ASN1InputStream(new ByteArrayInputStream(envDataKEK));

        info = ContentInfo.getInstance(aIn.readObject());
        envData = EnvelopedData.getInstance(info.getContent());
        s = envData.getRecipientInfos();

        recip = RecipientInfo.getInstance(s.getObjectAt(0));

        KEKRecipientInfo   inf = KEKRecipientInfo.getInstance(recip.getInfo());


        // Nested NDEF problem
        ASN1StreamParser asn1In = new ASN1StreamParser(new ByteArrayInputStream(envDataNestedNDEF));
        ContentInfoParser ci = new ContentInfoParser((ASN1SequenceParser)asn1In.readObject());
        EnvelopedDataParser ed = new EnvelopedDataParser((ASN1SequenceParser)ci
            .getContent(BERTags.SEQUENCE));
        ed.getVersion();
        ed.getOriginatorInfo();
        ed.getRecipientInfos().toASN1Primitive();
        EncryptedContentInfoParser eci = ed.getEncryptedContentInfo();
        eci.getContentType();
        eci.getContentEncryptionAlgorithm();
        */
    }

    public static void main(String[] args) {
        envelopedTest();
    }
}
