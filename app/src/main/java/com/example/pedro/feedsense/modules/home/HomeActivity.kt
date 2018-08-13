package com.example.pedro.feedsense.modules.home

import kotlinx.android.synthetic.main.activity_home.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.pedro.feedsense.R

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = HomeViewModel()

        submit_code_button.setOnClickListener { _ ->
            val pin = session_code_field.text.toString()
            viewModel.createSession(pin)
        }

        green_button.setOnClickListener { _ ->
            viewModel.printSomething()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}
