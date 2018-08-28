package com.example.pedro.feedsense.models

enum class Reaction(val reaction: String) {
    LOVING("Loving"),
    WHATEVER("Whatever"),
    HATING("Hating")
}

data class ReactionModel(val pin: String, val guestId: String, val guestComment: Reaction)