package com.example.pedro.feedsense.repository

import com.example.pedro.feedsense.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface NetworkServices {
    fun feedsenseService(): FeedsenseService
}

class RetrofitInitializer: NetworkServices {

    private val baseUrl: String = BuildConfig.BASE_URL

    private val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient().build())
            .baseUrl(baseUrl)
            .build()

    override fun feedsenseService(): FeedsenseService = retrofit.create(FeedsenseService::class.java)

    private fun getLoggin(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    private fun getHttpClient(): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(getLoggin())
        return httpClient
    }
}