package com.example.pedro.feedsense.modules

import com.example.pedro.feedsense.PreferenceHelper
import com.example.pedro.feedsense.PreferenceHelper.defaultPrefs
import com.example.pedro.feedsense.PreferenceHelper.get
import android.content.Intent
import android.app.Activity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.example.pedro.feedsense.modules.home.HomeActivity
import com.example.pedro.feedsense.modules.login.LoginActivity
import io.fabric.sdk.android.Fabric

class MainActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // starts Crashlytics for project
        Fabric.with(this, Crashlytics())

        val activityClass: Class<out Activity> = if (userIsLoggedOn())
            HomeActivity::class.java
        else
            LoginActivity::class.java

        val next = Intent(this, activityClass)
        finish()
        startActivity(next)
    }

    private fun userIsLoggedOn(): Boolean {
        val prefs = defaultPrefs(this)
        return prefs[PreferenceHelper.IS_LOGGED] ?: false
    }
}