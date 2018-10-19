package com.example.pedro.feedsense

import android.widget.LinearLayout

fun LinearLayout.setMargins(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val params: LinearLayout.LayoutParams = this.layoutParams as LinearLayout.LayoutParams

    val left = left?.let { it } ?: params.leftMargin
    val top = top?.let { it } ?: params.topMargin
    val right = right?.let { it } ?: params.rightMargin
    val bottom = bottom?.let { it } ?: params.bottomMargin

    params.setMargins(left, top, right, bottom)
    this.requestLayout()
}