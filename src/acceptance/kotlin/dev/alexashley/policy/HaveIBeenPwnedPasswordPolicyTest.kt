package dev.alexashley.policy

import khttp.get
import khttp.post
import khttp.put
import khttp.responses.Response
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HaveIBeenPwnedPasswordPolicyTest {
    companion object {
        private const val keycloakUri = "http://localhost:8080/auth"

        private const val keycloakAdminUsername = "keycloak"
        private const val keycloakAdminPassword = "password"

        private const val acceptanceUsername = "test"
    }

    private lateinit var accessToken: String
    private lateinit var userId: String

    @BeforeAll
    fun before() {
        accessToken = adminPasswordGrant(keycloakAdminUsername, keycloakAdminPassword)

        val user = findUserByUsername(acceptanceUsername)

        userId = user.getString("id")
    }

    @Test
    fun `should prevent the use of pwned passwords`() {
        val expectedError = "Please choose a different password. According to Have I Been Pwned"
        val pwnedPassword = "password"

        val updateResponse = updatePassword(userId, pwnedPassword)
        val error = updateResponse.jsonObject.getString("error")

        assertEquals(400, updateResponse.statusCode)
        assertNotNull(error)
        assertTrue(error!!.contains(expectedError))
    }

    @Test
    fun `should allow for strong passwords`() {
        val password = UUID.randomUUID().toString()

        val updateResponse = updatePassword(userId, password)

        assertEquals(204, updateResponse.statusCode)
    }

    private fun adminPasswordGrant(adminUser: String, adminPassword: String): String {
        val params = mapOf(
                "client_id" to "admin-cli",
                "grant_type" to "password",
                "username" to adminUser,
                "password" to adminPassword
        )
        val fullUri = "$keycloakUri/realms/master/protocol/openid-connect/token"
        val response = post(fullUri, data = params)

        return response.jsonObject.getString("access_token")!!
    }

    private fun findUserByUsername(username: String): JSONObject {
        val fullUri = "$keycloakUri/admin/realms/pwned/users"
        val response = get(
                fullUri,
                headers = mapOf(
                        "authorization" to "bearer $accessToken"
                ),
                params = mapOf("username" to username)
        )

        return response.jsonArray.first() as JSONObject
    }

    private fun updatePassword(userId: String, newPassword: String): Response {
        val fullUri = "$keycloakUri/admin/realms/pwned/users/$userId/reset-password"

        return put(
                fullUri,
                headers = mapOf(
                        "authorization" to "bearer $accessToken"
                ),
                json = mapOf(
                        "value" to newPassword,
                        "type" to "password"
                )
        )
    }
}
