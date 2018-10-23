package com.example.pedro.feedsense.modules.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pedro.feedsense.SingleLiveEvent
import com.example.pedro.feedsense.models.Alert
import com.example.pedro.feedsense.models.User
import com.example.pedro.feedsense.repository.NetworkServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val service: NetworkServices): ViewModel() {

    private var disposable: Disposable? = null

    private val _showHomeScreenForGuest = SingleLiveEvent<String>()
    val showHomeScreenForGuest: LiveData<String>
        get() = _showHomeScreenForGuest

    private val _showHomeScreenForUser = SingleLiveEvent<String>()
    val showHomeScreenForUser: LiveData<String>
        get() = _showHomeScreenForUser

    private val _stopLoading = SingleLiveEvent<Void>()
    val stopLoading: LiveData<Void>
        get() = _stopLoading

    private val _showAlert = SingleLiveEvent<Alert>()
    val showAlert: LiveData<Alert>
        get() = _showAlert

    fun joinSession(sessionId: String) {

        disposable = service.feedsenseService()
                .joinSession(sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { treatJoinSessionWithSuccess(sessionId) },
                        { error -> showErrorAlert(error) }
                )
    }

    fun treatJoinSessionWithSuccess(sessionId: String) {
        _showHomeScreenForGuest.value = sessionId
        _showHomeScreenForGuest.call()
    }

    fun performLogin(email: String, password: String) {
        val user = User(email, password, true, "")
        disposable = service.feedsenseService()
                .login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { token -> treatUserAuthorizationSuccess(token) },
                        { error -> showErrorAlert(error) }
                )

    }

    fun register(email: String, password: String, checkPassword: String) {
        if (password == checkPassword && !email.isEmpty() && !password.isEmpty()) {
            val user = User(email, password, true, "")
            performRegisterRequest(user)
            return
        }
        val alert = Alert("Oops!", "Preencha todos os campos corretamente!", "Ok")
        _showAlert.value = alert
        _showAlert.call()
        _stopLoading.call()
    }

    private fun performRegisterRequest(user: User) {
        disposable = service.feedsenseService()
                .registerUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { token -> treatUserAuthorizationSuccess(token) },
                        { error -> showErrorAlert(error) }
                )
    }

    private fun treatUserAuthorizationSuccess(token: String) {
        _showHomeScreenForUser.value = token
        _showHomeScreenForUser.call()
    }

    fun showErrorAlert(error: Throwable?) {
        val alert = Alert("Oops!", error?.message ?: "", "Ok")
        _showAlert.value = alert
        _showAlert.call()
        _stopLoading.call()
    }
}