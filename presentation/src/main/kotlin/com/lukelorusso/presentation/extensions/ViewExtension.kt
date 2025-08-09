package com.lukelorusso.presentation.extensions

import android.view.View
import android.view.ViewTreeObserver

fun View.setAlphaWithAnimation(
    start: Float,
    end: Float,
    duration: Long? = null,
    animationEnd: (() -> Unit)? = null
) = (start to end).applyFloatAnimation(duration = duration, animationEnd = animationEnd) {
    alpha = it
}

fun View.addOneTimeOnGlobalLayoutListener(listener: () -> Unit) {
    var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        listener()
        this.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }
    this.viewTreeObserver.addOnGlobalLayoutListener(
        onGlobalLayoutListener
    )
}
