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