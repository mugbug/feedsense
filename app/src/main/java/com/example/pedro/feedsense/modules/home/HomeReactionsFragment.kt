package com.example.pedro.feedsense.modules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.databinding.FragmentHomeReactionsBinding
import kotlinx.android.synthetic.main.fragment_home_reactions.*
import kotlinx.android.synthetic.main.fragment_line_chart.*
import org.koin.android.architecture.ext.sharedViewModel
import org.koin.android.architecture.ext.viewModel

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
            if (it != null) {
                (activity as? HomeActivity)?.showSimpleDialog(it)
                home_join_session_button.revertAnimation()
            }
        })

        viewModel.showToast.observe(this, Observer {
            if (it != null) {
                (activity as? HomeActivity)?.showToast(it)
            }
        })

        viewModel.hideJoinSessionFields.observe(this, Observer {
            shouldHideJoinSessionFields()
            reaction_buttons.visibility = View.VISIBLE
            home_join_session_button.revertAnimation()
        })
    }

    private fun shouldHideJoinSessionFields() {
        join_session_fields.visibility = View.GONE
        session_code_field.setText("")
        session_code_field.clearFocus()
    }
}