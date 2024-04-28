package com.mpcs.memorizeme.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import kotlinx.coroutines.delay

var HIGHLIGHT_INTERVAL: Long = 100L

fun View.nullableBackground(): Drawable? = background

suspend inline fun View.flash() {
    highlight()
    delay(HIGHLIGHT_INTERVAL)
    unhighlight()
}

fun View.unhighlight() {
    nullableBackground()?.let {
        it.alpha = 255
    } ?: run {
        visibility = View.VISIBLE
    }
}


fun View.highlight() {
    val nullableBackground = nullableBackground()
    if (nullableBackground != null) {
        nullableBackground.alpha = 50
    } else {
        visibility = View.INVISIBLE
    }
}
