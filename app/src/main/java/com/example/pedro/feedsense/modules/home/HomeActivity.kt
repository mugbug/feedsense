package com.example.pedro.feedsense.modules.home

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.LiveData
import com.example.pedro.feedsense.PreferenceHelper.defaultPrefs
import com.example.pedro.feedsense.PreferenceHelper.get
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.databinding.ActivityHomeBinding
import com.example.pedro.feedsense.models.Reaction
import com.example.pedro.feedsense.modules.BaseActivity
import com.example.pedro.feedsense.modules.login.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.architecture.ext.viewModel


class HomeActivity : BaseActivity() {

    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(
                this,
                R.layout.activity_home
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        checkHomeState()


        setupObservers()
    }

    private fun checkHomeState() {
        val prefs = defaultPrefs(this)
        val sessionId: String? = prefs["sessionId"]
        if (sessionId != null) {
            viewModel.setCurrentSession(sessionId)
            shouldShowReactionButtons()
        }
    }

    private fun setupObservers() {
        viewModel.showAlert.observe(this, Observer {
            if (it != null) {
                showSimpleDialog(it.title, it.message, it.buttonText, it.isCancelable)
            }
        })

        viewModel.showToast.observe(this, Observer {
            if (it != null) {
                showToast(it)
            }
        })

        viewModel.hideJoinSessionFields.observe(this, Observer {
            shouldHideJoinSessionFields()
        })
    }

    // Actions

    fun didTapLogout(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

//    @Suppress("UNUSED_PARAMETER")
//    fun didTapCreateSession(view: View) {
//        val pin = session_code_field.text.toString()
//        viewModel.createSession(pin)
//    }

    fun didTapWannaJoinSession(view: View) {
        join_session_fields.visibility = View.VISIBLE
    }

    private fun shouldHideJoinSessionFields() {
        join_session_fields.visibility = View.GONE
        session_code_field.setText("")
        session_code_field.clearFocus()
    }

    private fun shouldShowReactionButtons() {
        reaction_buttons.visibility = View.VISIBLE
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapJoinSession(view: View) {
        val pin = session_code_field.text.toString()
        viewModel.joinSession(pin)
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapGreenButton(view: View) {
        val reaction = Reaction.LOVING
        viewModel.reactToSession(reaction)
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapYellowButton(view: View) {
        val reaction = Reaction.WHATEVER
        viewModel.reactToSession(reaction)
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapRedButton(view: View) {
        val reaction = Reaction.HATING
        viewModel.reactToSession(reaction)
    }
}
