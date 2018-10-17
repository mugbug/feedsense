package com.example.pedro.feedsense

import android.app.Application
import com.example.pedro.feedsense.di.module
import org.koin.android.ext.android.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(module))
    }
}