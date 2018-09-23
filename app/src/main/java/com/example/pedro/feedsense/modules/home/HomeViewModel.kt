package com.example.pedro.feedsense.modules.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.pedro.feedsense.SingleLiveEvent
import com.example.pedro.feedsense.models.Alert
import com.example.pedro.feedsense.models.Reaction
import com.example.pedro.feedsense.models.ReactionModel
import com.example.pedro.feedsense.models.SessionModel
import com.example.pedro.feedsense.repository.NetworkServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class HomeViewModel(val service: NetworkServices): ViewModel() {

    var disposable: Disposable? = null
    private var currentSession: String = ""
    private val guestId = "pedro@zaroni.com"

    // Activity observables
    private val _showAlert = SingleLiveEvent<Alert>()
    val showAlert: LiveData<Alert>
        get() = _showAlert

    private val _showToast = SingleLiveEvent<String>()
    val showToast: LiveData<String>
        get() = _showToast

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
        val alert = Alert("Sucesso!", "Voce se conectou a sessao $sessionId", "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }

    private fun joinedSessionWithError(error: Throwable?) {
        print(error)
        val alert = Alert("Ops!", "Algo deu errado!\n" + error?.message, "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }

    fun createSession(sessionId: String) {
        val currentTime = Calendar.getInstance().time
        val sessionModel = SessionModel(sessionId, currentTime, "Pedro Zaroni")

        disposable = service.feedsenseService()
                .createSession(sessionModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _ -> treatCreateSessionWithSuccess(sessionId)},
                    { error -> treatCreateSessionWithFailure(error)}
                )
    }

    private fun treatCreateSessionWithFailure(error: Throwable?) {
        val alert = Alert("Ops!", "Algo deu errado!\n" + error?.message, "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }

    private fun treatCreateSessionWithSuccess(sessionId: String) {
        val alert = Alert("Sucesso!", "Sessao $sessionId criada com sucesso!", "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }

    fun reactToSession(reaction: Reaction) {
        if (currentSession.isEmpty()) {
            shouldJoinSession()
            return
        }
        val reactionModel = ReactionModel(currentSession, guestId, reaction)
        disposable = service.feedsenseService()
                .reactToSession(reactionModel)
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
        _showToast.value = "Reaçao enviada!"
        _showToast.call()
    }

    fun reactedToSessionWithError(error: Throwable?) {
        _showToast.value = "Oops! Algo deu errado!"
        _showToast.call()
    }
}