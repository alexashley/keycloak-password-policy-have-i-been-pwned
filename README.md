# keycloak-password-policy-have-i-been-pwned

A [Keycloak](https://www.keycloak.org/) password policy that checks potential passwords against [Have I Been Pwnd](https://haveibeenpwned.com).

![account password reset page][policy-message]

## installation

**Note**: this project was a weekend hack to try out the password policy SPI and HIBP API; it's not as robust or scalable as would be necessary in a production environment,  
It depends on an SPI defined in `keycloak-server-spi-private`, which is not part of the public interface and may break between versions. A warning will print at start-up:

```shell
 WARN  [org.keycloak.services] (ServerService Thread Pool -- 64) KC-SERVICES0047: password-policy-have-i-been-pwned (dev.alexashley.policy.HaveIBeenPwnedPasswordPolicyProviderFactory) is implementing the internal SPI password-policy. This SPI is internal and may change without notice
```


Build the jar with `make build` (see the [development](#development) section for prerequisites) and place it under `${KEYCLOAK_HOME}/standalone/deployments/`.

Now you should see the provider as an option in the dropdown, and can configure it:

![policy config][policy-config]

The policy value is the threshold for the number of times that the password hash appears in HIBP. The default is 1, meaning that any password that appears in HIBP is disallowed.

## development

### tools

- [`jabba`](https://github.com/shyiko/jabba)
- [`jq`](https://stedolan.github.io/jq/)

### running locally

- `jabba use`
- `make build` to create the jar
- `make keycloak` to start an instance of Keycloak with the policy
    - admin credentials: `keycloak:password`
    - user credentials: `test:password`
- `./scripts/init-script.sh` to setup the realm and user

[policy-message]: ./images/pwned.png "Account password reset page policy message"
[policy-config]: ./images/policy-config.png "Policy setup and config"
