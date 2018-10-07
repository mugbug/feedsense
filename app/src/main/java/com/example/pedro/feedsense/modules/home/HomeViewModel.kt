package com.example.pedro.feedsense.modules.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
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

class HomeViewModel(private val service: NetworkServices): ViewModel() {

    private var disposable: Disposable? = null
    private val guestId = "pedro@zaroni.com"

    private val _currentSession = MutableLiveData<String>()
    var currentSession: LiveData<String> = _currentSession
        get() = _currentSession

    // Activity Events
    private val _showAlert = SingleLiveEvent<Alert>()
    val showAlert: LiveData<Alert>
        get() = _showAlert

    private val _showToast = SingleLiveEvent<String>()
    val showToast: LiveData<String>
        get() = _showToast

    private val _hideJoinSessionFields = SingleLiveEvent<Void>()
    val hideJoinSessionFields: LiveData<Void>
        get() = _hideJoinSessionFields

    init {
        _currentSession.value = "-"
    }

    fun setCurrentSession(value: String) {
        _currentSession.value = value
    }

    fun joinSession(sessionId: String) {

        disposable = service.feedsenseService()
                .joinSession(sessionId, guestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> treatJoinSessionWithSuccess(sessionId) },
                        { error -> treatJoinSessionWithFailure(error) }
                )
    }

    private fun treatJoinSessionWithSuccess(sessionId: String) {
        _currentSession.value = sessionId
        val alert = Alert("Sucesso!", "Voce se conectou a sessao $sessionId", "Ok")
        _showAlert.value = alert
        _showAlert.call()
        _hideJoinSessionFields.call()
    }

    private fun treatJoinSessionWithFailure(error: Throwable?) {
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
        if ((_currentSession.value ?: "").isEmpty()) {
            shouldJoinSession()
            return
        }
        val reactionModel = ReactionModel(_currentSession.value ?: "", guestId, reaction)
        disposable = service.feedsenseService()
                .reactToSession(reactionModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> reactedToSessionWithSuccess() },
                        { error -> reactedToSessionWithError(error) }
                )
    }

    private fun shouldJoinSession() {
        val alert = Alert("Oops!", "E necessario entrar em uma sessao antes de reagir!", "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }

    private fun reactedToSessionWithSuccess() {
        _showToast.value = "Reaçao enviada!"
        _showToast.call()
    }

    private fun reactedToSessionWithError(error: Throwable?) {
        print(error)
        _showToast.value = "Oops! Algo deu errado!"
        _showToast.call()
    }
}