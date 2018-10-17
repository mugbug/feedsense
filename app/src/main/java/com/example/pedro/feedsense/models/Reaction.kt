package com.example.pedro.feedsense.models

import java.util.Date

enum class Reaction(val reaction: String) {
    LOVING("LOVING"),
    WHATEVER("WHATEVER"),
    HATING("HATING")
}

data class ReactionModel(
        val pin: String,
        val guestId: String,
        val guestComment: Reaction,
        val timestamp: Date)

data class ReactionEntry(
        val loving: Int,
        val whatever: Int,
        val hating: Int,
        val timestamp: Date)
