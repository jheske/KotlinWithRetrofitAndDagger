package com.example.commentsold.data.model

/**
 * Data class that captures user information for logged in users retrieved from Repository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String
)