MAKEFLAGS += --silent

.PHONY: keycloak keycloak-image build

default:
	echo "No default rule"

build:
	gradle shadowJar

keycloak-image:
	docker build -t keycloak-with-policy .

keycloak: keycloak-image
	docker run -p 8080:8080 -e KEYCLOAK_USER=keycloak -e KEYCLOAK_PASSWORD=password keycloak-with-policy
