FROM jboss/keycloak:6.0.1

COPY build/libs/keycloak-password-policy-have-i-been-pwned-all.jar \
    /opt/jboss/keycloak/standalone/deployments/keycloak-password-policy-have-i-been-pwned-all.jar
