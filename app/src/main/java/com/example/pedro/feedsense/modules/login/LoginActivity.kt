package com.example.pedro.feedsense.modules.login

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.databinding.ActivityLoginBinding
import com.example.pedro.feedsense.modules.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.architecture.ext.viewModel
import android.view.View
import android.content.Intent
import android.content.SharedPreferences
import com.squareup.picasso.Picasso
import androidx.lifecycle.Observer
import com.example.pedro.feedsense.PreferenceHelper.defaultPrefs
import com.example.pedro.feedsense.PreferenceHelper.set
import com.example.pedro.feedsense.PreferenceHelper.get
import com.example.pedro.feedsense.modules.home.HomeActivity

import com.crashlytics.android.Crashlytics
import com.example.pedro.feedsense.PreferenceHelper
import com.example.pedro.feedsense.modules.hideKeyboard
import io.fabric.sdk.android.Fabric

class LoginActivity: BaseActivity() {

    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        prefs = defaultPrefs(this)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
                this,
                R.layout.activity_login
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        updateBackgroundImage()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.showHomeScreenForGuest.observe(this, Observer {
            if (it != null) showHomeScreenForGuest(it)
        })

        viewModel.showHomeScreenForUser.observe(this, Observer {
            if (it != null) showHomeScreenForUser(it)
        })

        viewModel.stopLoading.observe(this, Observer {
            stopLoading()
        })

        viewModel.showAlert.observe(this, Observer {
            if (it != null) showSimpleDialog(it)
        })
    }

    private fun updateBackgroundImage() {
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        Picasso.get()
                .load("https://source.unsplash.com/collection/3333976")
                .fit()
                .centerCrop()
                .into(background_image)
    }

    private fun stopLoading() {
        login_button.revertAnimation()
        register_button.revertAnimation()
        join_session_button.revertAnimation()
    }

    fun didTapLogin(view: View) {
        hideKeyboard(this)
        login_button.startAnimation()
        val email = email_field.text.toString()
        val password = password_field.text.toString()
        viewModel.performLogin(email, password)
    }

    fun didTapRegister(view: View) {
        hideKeyboard(this)
        register_button.startAnimation()
        val email = email_field.text.toString()
        val password = password_field.text.toString()
        val checkPassword = password_check_field.text.toString()
        viewModel.register(email, password, checkPassword)
    }

    fun didTapJoinAsGuest(view: View) {
        hideKeyboard(this)
        join_session_button.startAnimation()
        val sessionId = login_session_id_field.text.toString()
        viewModel.joinSession(sessionId)
    }

    private fun showHomeScreenForGuest(sessionId: String) {
        prefs[PreferenceHelper.SESSION_ID] = sessionId
        prefs[PreferenceHelper.IS_LOGGED] = true

        goToHomeScreen()
    }

    private fun showHomeScreenForUser(email: String) {
        prefs[PreferenceHelper.EMAIL] = email
        prefs[PreferenceHelper.IS_LOGGED] = true

        goToHomeScreen()
    }

    private fun goToHomeScreen() {
        val next = Intent(this, HomeActivity::class.java)
        finish()
        startActivity(next)
    }

    fun didTapWannaRegister(view: View) {
        showRegisterFields()
    }

    fun didTapBackToLogin(view: View) {
        showLoginFields()
    }

    fun didTapWannaJoinAsGuest(view: View) {
        showJoinAsGuestFields()
    }

    fun showJoinAsGuestFields() {
        login_fields_title.text = "Entrar como visitante"
        login_fields_title.visibility = View.VISIBLE
        email_field.visibility = View.GONE
        password_field.visibility = View.GONE
        password_check_field.visibility = View.GONE
        login_session_id_field.visibility = View.VISIBLE
        login_button.visibility = View.GONE
        alt_buttons_layout.visibility = View.VISIBLE
        login_register_button_layout.visibility = View.GONE
        login_join_session_button_layout.visibility = View.VISIBLE
        start_register_button.visibility = View.VISIBLE
        join_as_guest_button.visibility = View.GONE
    }

    fun showRegisterFields() {
        login_fields_title.text = "Ainda nao tem cadastro?"
        login_fields_title.visibility = View.VISIBLE
        email_field.visibility = View.VISIBLE
        password_field.visibility = View.VISIBLE
        password_check_field.visibility = View.VISIBLE
        login_session_id_field.visibility = View.GONE
        login_button.visibility = View.GONE
        alt_buttons_layout.visibility = View.VISIBLE
        login_register_button_layout.visibility = View.VISIBLE
        login_join_session_button_layout.visibility = View.GONE
        start_register_button.visibility = View.GONE
        join_as_guest_button.visibility = View.VISIBLE
    }

    fun showLoginFields() {
        login_fields_title.visibility = View.GONE
        email_field.visibility = View.VISIBLE
        password_field.visibility = View.VISIBLE
        password_check_field.visibility = View.GONE
        login_session_id_field.visibility = View.GONE
        login_button.visibility = View.VISIBLE
        alt_buttons_layout.visibility = View.GONE
        login_register_button_layout.visibility = View.GONE
        login_join_session_button_layout.visibility = View.GONE
        start_register_button.visibility = View.VISIBLE
        join_as_guest_button.visibility = View.VISIBLE
    }
}