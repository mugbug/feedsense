package com.example.pedro.feedsense.modules.home

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentPagerAdapter
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.example.pedro.feedsense.*
import com.example.pedro.feedsense.PreferenceHelper.defaultPrefs
import com.example.pedro.feedsense.PreferenceHelper.get
import com.example.pedro.feedsense.databinding.ActivityHomeBinding
import com.example.pedro.feedsense.models.Reaction
import com.example.pedro.feedsense.modules.BaseActivity
import com.example.pedro.feedsense.modules.hideKeyboard
import com.example.pedro.feedsense.modules.login.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home_reactions.*
import kotlinx.android.synthetic.main.fragment_line_chart.*
import org.koin.android.architecture.ext.viewModel

class HomeActivity : BaseActivity() {

    val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(
                this,
                R.layout.activity_home
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        pager.adapter = HomePagerAdapter(supportFragmentManager)

        pager.post {
            pager.currentItem = 1
            checkHomeState()
        }
    }

    private fun checkHomeState() {
        val prefs = defaultPrefs(this)
        val sessionId: String? = prefs["sessionId"]
        if (sessionId != null) {
            viewModel.setCurrentSession(sessionId)
            showReactionButtons()
        }
    }

    // Actions

    fun didTapLogout(view: View) {
        defaultPrefs(this).edit().clear().apply()
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

    private fun showReactionButtons() {
        reaction_buttons.visibility = View.VISIBLE
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapJoinSession(view: View) {
        hideKeyboard(this)
        home_join_session_button.startAnimation()
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
