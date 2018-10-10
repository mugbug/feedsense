package com.example.pedro.feedsense.modules

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Hides keyboard from screen
 *
 * @param activity - Activity
 */
fun hideKeyboard(activity: Activity?) {
    activity?.let {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.findViewById<View>(android.R.id.content).windowToken, 0)
    }
}