MAKEFLAGS += --silent

.PHONY: acceptance keycloak keycloak-image build

default:
	echo "No default rule"

acceptance:
	./gradlew acceptance

build:
	./gradlew shadowJar

keycloak-image:
	docker build -t keycloak-with-policy .

keycloak: keycloak-image
	docker run	 -p 8080:8080 -e KEYCLOAK_USER=keycloak -e KEYCLOAK_PASSWORD=password keycloak-with-policy