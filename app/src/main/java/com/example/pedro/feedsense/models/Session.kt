package com.example.pedro.feedsense.models

import java.util.*

data class SessionModel(
        val pin: String,
        val time: Date,
        val owner: String,
        val isActive: Boolean)