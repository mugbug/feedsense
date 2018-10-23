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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

open class HomeViewModel(private val service: NetworkServices): ViewModel() {

    private var disposable: Disposable? = null

    var userToken = ""
    private val dateFormat = SimpleDateFormat("dd/M/yyyy HH:mm:ss")

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

    private val _updateSessionsSpinner = SingleLiveEvent<List<String>>()
    val updateSessionsSpinner: LiveData<List<String>?>
        get() = _updateSessionsSpinner

    init {
        _currentSession.value = "-"
    }

    fun setCurrentSession(value: String) {
        _currentSession.value = value
    }

    fun joinSession(sessionId: String) {

        disposable = service.feedsenseService(userToken)
                .joinSession(sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { treatJoinSessionWithSuccess(sessionId) },
                        { error -> showAlert(error) }
                )
    }

    private fun treatJoinSessionWithSuccess(sessionId: String) {
        _currentSession.value = sessionId
        val alert = Alert("Sucesso!", "Você se conectou à sessão $sessionId", "Ok")
        _showAlert.value = alert
        _showAlert.call()
        _hideJoinSessionFields.call()
    }

    fun createSession(sessionId: String) {
        val currentTime = Calendar.getInstance().time
        val sessionModel = SessionModel(sessionId, currentTime, userToken, true)

        disposable = service.feedsenseService(userToken)
                .createSession(sessionModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { treatCreateSessionWithSuccess(sessionId) },
                    { error -> showAlert(error) }
                )
    }

    private fun treatCreateSessionWithSuccess(sessionId: String) {
        _currentSession.value = sessionId
        joinSession(sessionId)
        val alert = Alert("Sucesso!", "Sessão $sessionId criada com sucesso!", "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }

    fun reactToSession(reaction: Reaction) {
        if ((_currentSession.value ?: "").isEmpty()) {
            shouldJoinSession()
            return
        }
        val currentTime = Calendar.getInstance().time
        val reactionModel = ReactionModel(_currentSession.value ?: "", reaction, currentTime)
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
        _showToast.value = "Obrigado pelo seu feedback!"
        _showToast.call()
    }

    private fun reactedToSessionWithError(error: Throwable?) {
        _showToast.value = error?.message ?: "Oops! Algo deu errado"
        _showToast.call()
    }

    fun parseReactionsToLineChartEntries(reactions: List<ReactionModel>): List<List<Entry>> {
        val chartData = ArrayList<List<Entry>>()
        val lovingEntries = ArrayList<Entry>()
        val whateverEntries = ArrayList<Entry>()
        val hatingEntries = ArrayList<Entry>()
        var lovingCount = 0f
        var whateverCount = 0f
        var hatingCount = 0f
        val startTime = reactions.firstOrNull()?.timestamp?.time ?: return chartData
        reactions.forEach {
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
        chartData.add(lovingEntries)
        chartData.add(whateverEntries)
        chartData.add(hatingEntries)
        return chartData
    }

    private fun refreshLineChartForReactions(reactions: List<ReactionModel>) {
        val sortedReactions = reactions.sortedBy { it.timestamp }
        val chartData = parseReactionsToLineChartEntries(sortedReactions)
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

    private fun updateSessionsSpinnerValues(sessions: List<SessionModel>) {
        val formattedSessions = sessions
                .sortedByDescending { it.time }
                .map {
            val formatedDate = dateFormat.format(it.time)
            it.pin + " - " + formatedDate
        }
        _updateSessionsSpinner.value = formattedSessions
        _updateSessionsSpinner.call()
    }

    fun lineGraphPageSelected() {
        disposable = service.feedsenseService(userToken)
                .fetchSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { sessions -> updateSessionsSpinnerValues(sessions) },
                        { error -> showAlert(error) }
                )
    }

    private fun showAlert(error: Throwable?) {
        val alert = Alert("Oops!", error?.message ?: "Algo deu errado", "Ok")
        _showAlert.value = alert
        _showAlert.call()
    }
}