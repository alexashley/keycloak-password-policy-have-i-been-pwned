package dev.alexashley.policy

import dev.alexashley.policy.services.HaveIBeenPwnedApiService
import dev.alexashley.policy.services.PasswordService
import org.keycloak.models.KeycloakContext
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.policy.PasswordPolicyProvider
import org.keycloak.policy.PolicyError

class HaveIBeenPwnedPassordPolicyProvider(
        private val passwordService: PasswordService,
        private val pwnedService: HaveIBeenPwnedApiService,
        private val context: KeycloakContext
) : PasswordPolicyProvider {
    override fun validate(username: String, password: String): PolicyError? {
        val passwordPwnThreshold: Int = context.realm.passwordPolicy.getPolicyConfig(HaveIBeenPwnedPasswordPolicyProviderFactory.providerId)
        val passwordHash = passwordService.hash(password)

        val pwnedPasswords = pwnedService.lookupPwndPasswordsByHash(passwordHash)

        val pwned = pwnedPasswords.firstOrNull {
            pwnedService.doesHashMatchPwnPassword(passwordHash, it) && it.pwnCount >= passwordPwnThreshold
        } ?: return null

        val formattedPwnCount = String.format("%,d", pwned.pwnCount)

        return PolicyError("""Please choose a different password. According to Have I Been Pwned, this password appears $formattedPwnCount times across a number of a data breaches. For more information, visit https://haveibeenpwned.com""")
    }

    override fun validate(realm: RealmModel, user: UserModel, password: String): PolicyError? = validate(user.username, password)

    override fun parseConfig(value: String): Int = parseInteger(value, 1)

    override fun close() {}
}
