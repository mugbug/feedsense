package com.example.pedro.feedsense.modules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pedro.feedsense.R
import kotlinx.android.synthetic.main.fragment_home_reactions.*
import org.koin.android.architecture.ext.viewModel

class HomeReactionsFragment: Fragment() {

    private val viewModel by viewModel<HomeViewModel>()

    companion object {
        fun newInstance(): HomeReactionsFragment {
            return HomeReactionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_reactions, container, false)
        setupObservers()
        return view
    }


    fun setupObservers() {
        viewModel.showAlert.observe(this, Observer {
            if (it != null) {
                (activity as? HomeActivity)?.showSimpleDialog(it.title, it.message, it.buttonText, it.isCancelable)
            }
        })

        viewModel.showToast.observe(this, Observer {
            if (it != null) {
                (activity as? HomeActivity)?.showToast(it)
            }
        })

        viewModel.hideJoinSessionFields.observe(this, Observer {
            shouldHideJoinSessionFields()
        })
    }

    private fun shouldHideJoinSessionFields() {
        join_session_fields.visibility = View.GONE
        session_code_field.setText("")
        session_code_field.clearFocus()
    }
}