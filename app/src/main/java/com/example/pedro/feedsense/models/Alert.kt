package com.example.pedro.feedsense.models

data class Alert(
        val title: String,
        val message: String,
        val buttonText: String,
        val isCancelable: Boolean = true
)