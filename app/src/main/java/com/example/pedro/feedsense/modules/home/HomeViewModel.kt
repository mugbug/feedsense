package com.example.pedro.feedsense.modules.home

import android.arch.lifecycle.ViewModel
import com.example.pedro.feedsense.models.Reaction
import com.example.pedro.feedsense.models.ReactionModel
import com.example.pedro.feedsense.models.SessionModel
import com.example.pedro.feedsense.repository.NetworkServices
import com.example.pedro.feedsense.repository.RetrofitInitializer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class HomeViewModel(val service: NetworkServices): ViewModel() {

    var disposable: Disposable? = null
    private var currentSession: String = ""

    private val guestId = "1020"

    fun joinSession(sessionId: String) {

        disposable = service.feedsenseService()
                .joinSession(sessionId, guestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> joinedSessionWithSuccess(sessionId) },
                        { error -> joinedSessionWithError(error) }
                )
    }

    private fun joinedSessionWithSuccess(sessionId: String) {
        currentSession = sessionId
    }

    private fun joinedSessionWithError(error: Any) {
        print(error)
    }

    fun createSession(pin: String) {
        val currentTime = Calendar.getInstance().time
        val sessionModel = SessionModel(pin, currentTime, "Pedro Zaroni")

        disposable = service.feedsenseService()
                .createSession(sessionModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> print("Session created\n $result") },
                    { error -> print("Failed to create session!\n Error: $error")}
                )
    }

    fun reactToSession(reaction: Reaction) {
        if (currentSession.isEmpty()) {
            shouldJoinSession()
            return
        }
        val reactionModel = ReactionModel(currentSession, guestId, reaction)
        disposable = service.feedsenseService()
                .reactToSession(currentSession, guestId, reactionModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> reactedToSessionWithSuccess() },
                        { error -> reactedToSessionWithError(error) }
                )
    }

    fun shouldJoinSession() {
        // show some feedback
    }

    fun reactedToSessionWithSuccess() {
        // show some feedback
    }

    fun reactedToSessionWithError(error: Any) {
        // show some feedback
    }
}