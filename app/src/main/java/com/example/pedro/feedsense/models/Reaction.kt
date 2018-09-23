package com.example.pedro.feedsense.models

enum class Reaction(val reaction: String) {
    LOVING("LOVING"),
    WHATEVER("WHATEVER"),
    HATING("HATING")
}

data class ReactionModel(val pin: String, val guestId: String, val guestComment: Reaction)