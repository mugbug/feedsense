package com.example.pedro.feedsense.models

data class User(val email: String,
                val password: String,
                val isUser: Boolean,
                val pinSession: String)