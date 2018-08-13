package com.example.pedro.feedsense.modules.home

import com.example.pedro.feedsense.models.SessionModel
import com.example.pedro.feedsense.repository.RetrofitInitializer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class HomeViewModel {

    var disposable: Disposable? = null

    fun createSession(pin: String) {
        val currentTime = Calendar.getInstance().time
        val sessionModel = SessionModel(pin, currentTime, "Pedro Zaroni")

        disposable = RetrofitInitializer().feedsenseService()
                .createSession(sessionModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> print("Session created\n $result") },
                    { error -> print("Failed to create session!\n Error: $error")}
                )
    }

    fun printSomething() {
        disposable = RetrofitInitializer().feedsenseService()
                .listSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> printSessions(result)},
                    { error -> print("Failed to fetch sessions!\n Error: $error")}
                )
    }

    private fun printSessions(sessions: List<SessionModel>?) {
        sessions?.forEach {
            print("$it")
        }
    }

    fun onPause() {
        disposable?.dispose()
    }
}