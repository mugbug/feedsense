package com.example.pedro.feedsense.repository

import com.example.pedro.feedsense.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.fabric.sdk.android.services.settings.IconRequest.build
import okhttp3.Credentials


interface NetworkServices {
    fun feedsenseService(auth: String? = null): FeedsenseService
}

class RetrofitInitializer: NetworkServices {

    private val baseUrl: String = BuildConfig.BASE_URL

    private var httpClient = OkHttpClient.Builder()

    private val retrofitBuilder = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)

    override fun feedsenseService(authToken: String?): FeedsenseService {


        if (authToken != null && !authToken.isEmpty()) {
            val interceptor = Interceptor { chain ->
                val originalRequest = chain.request()

                val builder = originalRequest.newBuilder().header("Authorization", authToken)

                val newRequest = builder.build()
                chain.proceed(newRequest)
            }
            httpClient.addInterceptor(interceptor)
        }
        httpClient.addInterceptor(getLoggin())

        return retrofitBuilder
                .client(httpClient.build())
                .build()
                .create(FeedsenseService::class.java)
    }

    private fun getLoggin(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }
}