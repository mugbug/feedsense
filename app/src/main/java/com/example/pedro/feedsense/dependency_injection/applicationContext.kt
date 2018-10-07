package com.example.pedro.feedsense.dependency_injection

import com.example.pedro.feedsense.modules.home.HomeViewModel
import com.example.pedro.feedsense.modules.login.LoginViewModel
import com.example.pedro.feedsense.repository.NetworkServices
import com.example.pedro.feedsense.repository.RetrofitInitializer
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val module: Module = applicationContext {

    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }

    bean { RetrofitInitializer() as NetworkServices }
}