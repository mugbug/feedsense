package com.example.pedro.feedsense

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_home)
    }
}
