package com.example.pedro.feedsense.modules.home

import kotlinx.android.synthetic.main.activity_home.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.example.pedro.feedsense.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_home)

        green_button.setOnClickListener { _ ->

        }
    }
}
