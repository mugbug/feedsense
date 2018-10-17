package com.example.pedro.feedsense.modules.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pedro.feedsense.SingleLiveEvent
import com.example.pedro.feedsense.models.*
import com.example.pedro.feedsense.repository.NetworkServices
import com.github.mikephil.charting.data.Entry
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit

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

    private val _configureLineChart = SingleLiveEvent<List<List<Entry>>?>()
    val configureLineChart: LiveData<List<List<Entry>>?>
        get() = _configureLineChart

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
                        { error -> showAlert(error) }
                )
    }

    private fun treatJoinSessionWithSuccess(sessionId: String) {
        _currentSession.value = sessionId
        val alert = Alert("Sucesso!", "Voce se conectou a sessao $sessionId", "Ok")
        _showAlert.value = alert
        _showAlert.call()
        _hideJoinSessionFields.call()
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
                    { error -> showAlert(error)}
                )
    }

    private fun treatCreateSessionWithSuccess(sessionId: String) {
        val alert = Alert("Sucesso!", "sessão $sessionId criada com sucesso!", "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }

    fun reactToSession(reaction: Reaction) {
        if ((_currentSession.value ?: "").isEmpty()) {
            shouldJoinSession()
            return
        }
        val currentTime = Calendar.getInstance().time
        val reactionModel = ReactionModel(_currentSession.value ?: "", guestId, reaction, currentTime)
        disposable = service.feedsenseService()
                .reactToSession(reactionModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { reactedToSessionWithSuccess() },
                        { error -> reactedToSessionWithError(error) }
                )
    }

    private fun shouldJoinSession() {
        val alert = Alert("Oops!", "É necessário entrar em uma sessão antes de reagir!", "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }

    private fun reactedToSessionWithSuccess() {
        _showToast.value = "Reação enviada!"
        _showToast.call()
    }

    private fun reactedToSessionWithError(error: Throwable?) {
        _showToast.value = error?.message ?: "Oops! Algo deu errado"
        _showToast.call()
    }

    private fun refreshLineChartForReactions(reactions: List<ReactionModel>) {
        val sortedReactions = reactions.sortedBy { it.timestamp }
        var lovingEntries = ArrayList<Entry>()
        var whateverEntries = ArrayList<Entry>()
        var hatingEntries = ArrayList<Entry>()
        var lovingCount = 0f
        var whateverCount = 0f
        var hatingCount = 0f
        var startTime = sortedReactions[0].timestamp.time
        sortedReactions.forEach {
            val timestamp = TimeUnit.MILLISECONDS.toSeconds((it.timestamp.time - startTime)).toFloat()
            when (it.guestComment) {
                Reaction.LOVING -> lovingCount += 1
                Reaction.WHATEVER -> whateverCount += 1
                Reaction.HATING -> hatingCount += 1
            }
            lovingEntries.add(Entry(timestamp, lovingCount))
            whateverEntries.add(Entry(timestamp, whateverCount))
            hatingEntries.add(Entry(timestamp, hatingCount))
        }
        val chartData = ArrayList<List<Entry>>()
        chartData.add(lovingEntries)
        chartData.add(whateverEntries)
        chartData.add(hatingEntries)
        _configureLineChart.value = chartData
        _configureLineChart.call()
    }

    fun fetchReactions(sessionId: String) {
        disposable = service.feedsenseService()
                .fetchReactions(sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { reactions -> refreshLineChartForReactions(reactions) },
                        { error -> showAlert(error) }
                )
    }

    private fun showAlert(error: Throwable?) {
        val alert = Alert("Oops!", error?.message ?: "Algo deu errado", "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }
}