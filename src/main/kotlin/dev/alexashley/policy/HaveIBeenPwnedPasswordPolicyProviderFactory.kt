package dev.alexashley.policy

import dev.alexashley.policy.services.HaveIBeenPwnedApiService
import dev.alexashley.policy.services.PasswordService
import org.keycloak.Config
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.policy.PasswordPolicyProvider
import org.keycloak.policy.PasswordPolicyProviderFactory

class HaveIBeenPwnedPasswordPolicyProviderFactory : PasswordPolicyProviderFactory {
    companion object {
        const val providerId = "password-policy-have-i-been-pwned"
    }

    // The only config is the minimum number of times the password must appear in the Pwnd Passwords dataset for it to be a policy violation.
    override fun getDefaultConfigValue() = "1"

    override fun getConfigType() = PasswordPolicyProvider.INT_CONFIG_TYPE

    override fun getId() = providerId

    override fun getDisplayName() = "Have I Been Pwned?"

    override fun create(session: KeycloakSession): PasswordPolicyProvider = HaveIBeenPwnedPassordPolicyProvider(
            PasswordService(),
            HaveIBeenPwnedApiService(),
            session.context
    )

    override fun init(config: Config.Scope) {}

    override fun isMultiplSupported() = false

    override fun postInit(factory: KeycloakSessionFactory) {}

    override fun close() {}
}