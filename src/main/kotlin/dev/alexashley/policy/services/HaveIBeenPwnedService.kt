package dev.alexashley.policy.services

import dev.alexashley.policy.PwnedPassword
import khttp.get

class HaveIBeenPwnedApiService {
    companion object {
        private const val apiVersionContentType = "application/vnd.haveibeenpwned.v2+json"
        private const val serviceUri = "https://api.pwnedpasswords.com"
    }

    fun lookupPwndPasswordsByHash(hash: String): Sequence<PwnedPassword> {
        val hashPrefix = hash.slice(0..4)
        val response = get("$serviceUri/range/$hashPrefix", stream = true, headers = mapOf(
                "User-Agent" to "keycloak-password-policy-have-i-been-pwned",
                "Accept" to apiVersionContentType
        ))
        val responseBuffer = StringBuilder()

        for (chunk in response.contentIterator()) {
            responseBuffer.append(chunk.asSequence().map { it.toChar() }.joinToString(""))
        }

        return responseBuffer
                .splitToSequence("\n")
                .map {
                    val (hashSuffix, pwnCount) = it.trim().split(":")

                    PwnedPassword(hashSuffix, pwnCount.toInt())
                }
    }

    fun doesHashMatchPwnPassword(hash: String, pwndPassword: PwnedPassword): Boolean {
        val hashSuffix = hash.slice(5 until hash.length)

        return hashSuffix.compareTo(pwndPassword.hashSuffix, true) == 0
    }
}