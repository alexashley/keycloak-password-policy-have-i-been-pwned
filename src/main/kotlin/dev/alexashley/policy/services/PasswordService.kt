package dev.alexashley.policy.services

import java.security.MessageDigest
import java.util.*

class PasswordService {
    fun hash(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA1")
        val hash = messageDigest.digest(password.toByteArray(Charsets.UTF_8))

        return hexEncode(hash)
    }

    private fun hexEncode(bytes: ByteArray): String {
        val formatter = Formatter()

        formatter.use {
            bytes.forEach {
                formatter.format("%02x", it)
            }

            return formatter.toString()
        }
    }
}