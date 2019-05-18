# keycloak-password-policy-have-i-been-pwned

A [Keycloak](https://www.keycloak.org/) password policy that checks potential passwords against [Have I Been Pwnd](https://haveibeenpwned.com).

![account password reset page][policy-message]

[policy-message]: ./images/pwned.png "Account password reset page policy message"

## development

### tools

- [`gradle`](https://gradle.org/)
- [`jabba`](https://github.com/shyiko/jabba)
- [`jq`](https://stedolan.github.io/jq/)

### running locally

- `jabba use`
- `make build` to create the jar
- `make keycloak` to start an instance of Keycloak with the policy
    - admin credentials: `keycloak:password`
    - user credentials: `test:password`
- `./scripts/init-script.sh` to setup the realm and user
