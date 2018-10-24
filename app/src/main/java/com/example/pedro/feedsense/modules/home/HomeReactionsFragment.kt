package com.example.pedro.feedsense.modules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.databinding.FragmentHomeReactionsBinding
import kotlinx.android.synthetic.main.fragment_home_reactions.*
import org.koin.android.architecture.ext.sharedViewModel

class HomeReactionsFragment: Fragment() {

    val viewModel: HomeViewModel by sharedViewModel()

    companion object {
        fun newInstance(): HomeReactionsFragment {
            return HomeReactionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeReactionsBinding>(inflater, R.layout.fragment_home_reactions, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObservers()
    }

    fun setupObservers() {
        viewModel.showAlert.observe(this, Observer {
            if (it != null) (activity as? HomeActivity)?.showSimpleDialog(it)
            if (home_join_session_button.isAnimating) home_join_session_button.revertAnimation()
            if (home_create_session_button.isAnimating) home_create_session_button.revertAnimation()
        })

        viewModel.showToast.observe(this, Observer {
            if (it != null) (activity as? HomeActivity)?.showToast(it)
            if (green_button.isAnimating) green_button.revertAnimation()
            if (yellow_button.isAnimating) yellow_button.revertAnimation()
            if (red_button.isAnimating) red_button.revertAnimation()
        })

        viewModel.updateJoinSessionSpinner.observe(this, Observer {
            if (it != null) updateSessionsSpinner(it)
//            if (home_join_session_button.isAnimating) home_join_session_button.revertAnimation()
        })

        viewModel.hideJoinSessionFields.observe(this, Observer {
            shouldHideJoinSessionFields()
            reaction_buttons.visibility = View.VISIBLE
            if (home_join_session_button.isAnimating) home_join_session_button.revertAnimation()
            if (home_create_session_button.isAnimating) home_create_session_button.revertAnimation()
        })
    }

    private fun shouldHideJoinSessionFields() {
        join_session_fields.visibility = View.GONE
        session_code_field.setText("")
        session_code_field.clearFocus()
    }

    private fun updateSessionsSpinner(sessions: List<String>) {
        val context = activity!!.applicationContext

        val adapter = ArrayAdapter<String>(context, R.layout.spinner_item, sessions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        active_sessions_spinner.adapter = adapter
    }
}