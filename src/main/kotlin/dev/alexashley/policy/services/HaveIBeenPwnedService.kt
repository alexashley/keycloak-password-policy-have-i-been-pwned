package dev.alexashley.policy.services

import dev.alexashley.policy.PwnedPassword
import khttp.get

class HaveIBeenPwnedApiException(message: String) : Exception(message)

class HaveIBeenPwnedApiService {
    companion object {
        private const val apiVersionContentType = "application/vnd.haveibeenpwned.v2+json"
        private const val serviceUri = "https://api.pwnedpasswords.com"
    }

    fun lookupPwndPasswordsByHash(hash: String): Sequence<PwnedPassword> {
        val hashPrefix = hash.slice(0..4)
        val path = "/range/$hashPrefix"
        val response = get("$serviceUri$path", stream = true, headers = mapOf(
                "User-Agent" to "keycloak-password-policy-have-i-been-pwned",
                "Accept" to apiVersionContentType
        ))
        val responseBuffer = StringBuilder()

        for (chunk in response.contentIterator()) {
            responseBuffer.append(chunk.asSequence().map { it.toChar() }.joinToString(""))
        }

        if (response.statusCode != 200) {
            throw HaveIBeenPwnedApiException("Non-200 response from $path. Status code: ${response.statusCode}. Body: $responseBuffer")
        }

        return responseBuffer
                .splitToSequence("\n")
                .map {
                    val (hashSuffix, pwnCount) = it.trim().split(":")

                    PwnedPassword(hashSuffix, pwnCount.toInt())
                }
    }

    fun hashMatchesPwnedPassword(hash: String, pwndPassword: PwnedPassword): Boolean {
        val hashSuffix = hash.slice(5 until hash.length)

        return hashSuffix.compareTo(pwndPassword.hashSuffix, true) == 0
    }
}