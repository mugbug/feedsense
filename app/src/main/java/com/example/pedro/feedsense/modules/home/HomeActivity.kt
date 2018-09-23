package com.example.pedro.feedsense.modules.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_home.*
import android.os.Bundle
import android.view.View
import org.koin.android.architecture.ext.viewModel
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.databinding.ActivityHomeBinding
import com.example.pedro.feedsense.models.Reaction
import com.example.pedro.feedsense.modules.BaseActivity

class HomeActivity : BaseActivity() {

    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        setupObservers()
    }

    fun setupObservers() {
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
    }

    // Actions

    @Suppress("UNUSED_PARAMETER")
    fun didTapCreateSession(view: View) {
        val pin = session_code_field.text.toString()
        viewModel.createSession(pin)
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
