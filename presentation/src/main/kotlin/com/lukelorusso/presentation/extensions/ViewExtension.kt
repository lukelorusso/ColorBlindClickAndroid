package com.lukelorusso.presentation.extensions

import android.os.Build
import android.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lukelorusso.presentation.R

fun View.setAlphaWithAnimation(
    start: Float,
    end: Float,
    duration: Long? = null,
    animationEnd: (() -> Unit)? = null
) = (start to end).applyFloatAnimation(duration = duration, animationEnd = animationEnd) {
    alpha = it
}

fun View.fadeInView(duration: Int? = null) {
    this.animation?.setAnimationListener(null)
    this.animation?.cancel()
    this.clearAnimation()
    val effectDuration = duration ?: resources.getInteger(R.integer.fading_effect_duration_default)
    this.visibility = View.INVISIBLE // setting precondition (just in case)
    this.setAlphaWithAnimation(0F, 1F, effectDuration.toLong()) // setting animation
    if (this is FloatingActionButton) this.show() // custom behaviour
    else this.visibility = View.VISIBLE // making this visible to show the animation to the user
}

fun View.fadeOutView(duration: Int? = null) {
    this.animation?.setAnimationListener(null)
    this.animation?.cancel()
    this.clearAnimation()
    val effectDuration = duration ?: resources.getInteger(R.integer.fading_effect_duration_default)
    this.visibility = View.VISIBLE // setting precondition (just in case)
    this.setAlphaWithAnimation(
        1F,
        0F,
        effectDuration.toLong()
    ) // setting animation with listener at the end (if reached)
    if (this is FloatingActionButton) this.hide() // custom behaviour
    else this.visibility = View.INVISIBLE // making this invisible
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

@Suppress("DEPRECATION")
fun View.statusBarHeight() = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
        rootWindowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).top

    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
        rootWindowInsets?.systemWindowInsetTop ?: 0

    else ->
        0
}
