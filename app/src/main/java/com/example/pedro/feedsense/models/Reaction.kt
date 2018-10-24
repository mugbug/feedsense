package com.example.pedro.feedsense.models

import java.util.Date

enum class Reaction(val reaction: String) {
    LOVING("LOVING"),
    WHATEVER("WHATEVER"),
    HATING("HATING")
}

data class ReactionModel(
        val pin: String,
        val guestComment: Reaction,
        val timestamp: Date)

data class ReactionPercentage(
        val loving: Float = 0.333f,
        val whatever: Float = 0.333f,
        val hating: Float = 0.334f
)