package com.example.pedro.feedsense.repository

import com.google.gson.JsonParser
import retrofit2.HttpException

class ApiError constructor(error: Throwable) {
    var message = "Algo de errado ocorreu!"

    init {
        if (error is HttpException) {
            val errorJsonString = error.response()
                    .errorBody()?.string()
            try {
                this.message = JsonParser().parse(errorJsonString)
                        .asJsonObject["message"]
                        .asString
            } catch (e: Exception) { }
        } else {
            this.message = error.message ?: this.message
        }
    }
}