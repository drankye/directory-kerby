java -cp lib/\* -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8011 \
     -Djava.security.krb5.realm=SH.INTEL.COM \
     -Djava.security.krb5.kdc=zkdesk.sh.intel.com \
     -Djavax.security.auth.useSubjectCredsOnly=false \
     -Djava.security.auth.login.config=login.conf \
     security.samples.sasl.SaslSampleClient \
     zkdesk.sh.intel.com 8080 \
     myservice zkdesk.sh.intel.com