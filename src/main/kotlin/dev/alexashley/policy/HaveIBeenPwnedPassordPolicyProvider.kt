package dev.alexashley.policy

import org.keycloak.models.KeycloakContext
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.policy.PasswordPolicyProvider
import org.keycloak.policy.PolicyError

class HaveIBeenPwnedPassordPolicyProvider(
        private val context: KeycloakContext
) : PasswordPolicyProvider {
    override fun validate(username: String, password: String): PolicyError? {
        val passwordPwnThreshold: Int = context.realm.passwordPolicy.getPolicyConfig(HaveIBeenPwnedPasswordPolicyProviderFactory.providerId)

        println("got em $passwordPwnThreshold")

        return null
    }

    override fun validate(realm: RealmModel, user: UserModel, password: String): PolicyError? = validate(user.username, password)

    override fun parseConfig(value: String): Int = parseInteger(value, 1)

    override fun close() {}
}
