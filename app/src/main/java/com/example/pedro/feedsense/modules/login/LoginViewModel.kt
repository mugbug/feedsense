package com.example.pedro.feedsense.modules.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pedro.feedsense.SingleLiveEvent
import com.example.pedro.feedsense.models.Alert
import com.example.pedro.feedsense.repository.NetworkServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val service: NetworkServices): ViewModel() {

    private var disposable: Disposable? = null

    private val _showHomeScreen = SingleLiveEvent<String>()
    val showHomeScreen: LiveData<String>
        get() = _showHomeScreen

    fun joinSession(sessionId: String) {

        disposable = service.feedsenseService()
                .joinSession(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> treatJoinSessionWithSuccess(sessionId) },
                        { error -> treatJoinSessionWithFailure(error) }
                )
    }

    fun treatJoinSessionWithSuccess(sessionId: String) {
        _showHomeScreen.value = sessionId
        _showHomeScreen.call()
    }


    private fun treatJoinSessionWithFailure(error: Throwable?) {
        _showHomeScreen.value = "123"
        _showHomeScreen.call()
        Log.e("tag", error?.message)
    }

    fun performLogin(email: String, password: String) {
        // perform request for login
        _showHomeScreen.call()
    }
}