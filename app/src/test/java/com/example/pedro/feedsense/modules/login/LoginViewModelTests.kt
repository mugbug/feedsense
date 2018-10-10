package com.example.pedro.feedsense.modules.login

import com.example.pedro.feedsense.repository.NetworkServices
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import org.junit.Test

class LoginViewModelTests {

    val service: NetworkServices = mock()
    val view: LoginActivity = mock()

    @Test
    fun testTreatJoinSessionWithSuccess() {
        val viewModel = LoginViewModel(service)

        given(service.feedsenseService().registerUser(any()))
                .willReturn(Observable.just(any()))

        viewModel.register("a@a.com", "1", "1")

        verify(view).showHomeScreen()
    }
}