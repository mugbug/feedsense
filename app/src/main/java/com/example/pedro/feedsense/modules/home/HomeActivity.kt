package com.example.pedro.feedsense.modules.home

import kotlinx.android.synthetic.main.activity_home.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.example.pedro.feedsense.R

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = HomeViewModel()

        green_button.setOnClickListener { _ ->
            viewModel.printSomething()
        }
    }
}
