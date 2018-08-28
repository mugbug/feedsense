package com.example.pedro.feedsense.repository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface NetworkServices {
    fun feedsenseService(): FeedsenseService
}

class RetrofitInitializer: NetworkServices {

    val baseUrl: String = "https://api-tcc-rtfs.herokuapp.com/"

    val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient().build())
            .baseUrl(baseUrl)
            .build()

    override fun feedsenseService() = retrofit.create(FeedsenseService::class.java)

    fun getLoggin(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    fun getHttpClient(): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(getLoggin())
        return httpClient
    }
}