java -cp lib/haox-asn1-1.0-SNAPSHOT.jar:output \
     -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8010 \
     -Djava.security.krb5.realm=SH.INTEL.COM \
     -Djava.security.krb5.kdc=zkdev.sh.intel.com \
     -Djavax.security.auth.useSubjectCredsOnly=false \
     -Djava.security.auth.login.config=login.conf \
     SampleServer 8080