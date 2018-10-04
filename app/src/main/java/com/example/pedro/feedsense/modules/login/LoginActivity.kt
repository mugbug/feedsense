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
import com.squareup.picasso.Picasso
import android.view.WindowManager
import com.example.pedro.feedsense.modules.home.HomeActivity

class LoginActivity: BaseActivity() {

    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
                this,
                R.layout.activity_login
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        updateBackgroundImage()
    }

    private fun updateBackgroundImage() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        Picasso.get()
                .load("https://source.unsplash.com/featured/?nature,water")
                .fit()
                .centerCrop()
                .into(background_image)
    }

    fun didTapLogin(view: View) {
        // switch login button to loading
        // call presenter to do request
        showHomeScreen()
    }

    fun showHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
//        finish()
        startActivity(intent)
    }

    fun didTapRegister(view: View) {
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
        session_id_field.visibility = View.VISIBLE
        login_button.visibility = View.GONE
        alt_buttons_layout.visibility = View.VISIBLE
        register_button.visibility = View.GONE
        join_session_button.visibility = View.VISIBLE
        start_register_button.visibility = View.VISIBLE
        join_as_guest_button.visibility = View.GONE
    }

    fun showRegisterFields() {
        login_fields_title.text = "Ainda nao tem cadastro?"
        login_fields_title.visibility = View.VISIBLE
        email_field.visibility = View.VISIBLE
        password_field.visibility = View.VISIBLE
        password_check_field.visibility = View.VISIBLE
        session_id_field.visibility = View.GONE
        login_button.visibility = View.GONE
        alt_buttons_layout.visibility = View.VISIBLE
        register_button.visibility = View.VISIBLE
        join_session_button.visibility = View.GONE
        start_register_button.visibility = View.GONE
        join_as_guest_button.visibility = View.VISIBLE
    }

    fun showLoginFields() {
        login_fields_title.visibility = View.GONE
        email_field.visibility = View.VISIBLE
        password_field.visibility = View.VISIBLE
        password_check_field.visibility = View.GONE
        session_id_field.visibility = View.GONE
        login_button.visibility = View.VISIBLE
        alt_buttons_layout.visibility = View.GONE
        register_button.visibility = View.GONE
        join_session_button.visibility = View.GONE
        start_register_button.visibility = View.VISIBLE
        join_as_guest_button.visibility = View.VISIBLE
    }
}