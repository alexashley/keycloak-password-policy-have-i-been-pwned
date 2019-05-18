#!/usr/bin/env bash

set -eu

REALM="pwned"
KEYCLOAK=http://localhost:8080
ADMIN_USER=keycloak
ADMIN_PASS=password

USERNAME="test"
PASSWORD="password"

if [[ $( curl -s -w "%{http_code}" -o /dev/null "${KEYCLOAK}/auth/realms/${REALM}") -eq 200 ]]; then
    echo "Skipping realm setup"
    exit 0
fi

accessToken=$(curl "${KEYCLOAK}/auth/realms/master/protocol/openid-connect/token" \
                -H "Content-Type: application/x-www-form-urlencoded" \
                -s \
                --fail \
                -d "client_id=admin-cli" \
                -d "grant_type=password" \
                -d "username=${ADMIN_USER}" \
                -d "password=${ADMIN_PASS}" | jq -r '.access_token')

curl "${KEYCLOAK}/auth/admin/realms" \
                -H "Content-Type: application/json" \
                -H "Authorization: bearer ${accessToken}" \
                -s \
                --fail \
                -d '{"enabled": true, "realm": "'${REALM}'", "displayName": "'${REALM}'"}'

curl "${KEYCLOAK}/auth/admin/realms/${REALM}/users" \
                -H "Content-Type: application/json" \
                -H "Authorization: bearer ${accessToken}" \
                -s \
                --fail \
                -d '{"enabled": true, "username": "'${USERNAME}'", "credentials": [{"value": "'${PASSWORD}'", "temporary": false, "type": "password"}]}'
