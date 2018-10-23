package com.example.pedro.feedsense

import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

fun LinearLayout.setMargins(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val params: ConstraintLayout.LayoutParams = this.layoutParams as ConstraintLayout.LayoutParams

    val left = left?.let { it } ?: params.leftMargin
    val top = top?.let { it } ?: params.topMargin
    val right = right?.let { it } ?: params.rightMargin
    val bottom = bottom?.let { it } ?: params.bottomMargin

    params.setMargins(left, top, right, bottom)
    this.requestLayout()
}