package dev.alexashley.policy

data class PwnedPassword(val hashedPassword: String, val pwnCount: Int)