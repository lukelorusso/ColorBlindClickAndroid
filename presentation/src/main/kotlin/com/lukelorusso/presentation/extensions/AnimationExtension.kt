package com.lukelorusso.presentation.extensions

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Handler

fun Pair<Float, Float>.applyFloatAnimation(
    duration: Long? = null,
    timeInterpolator: TimeInterpolator? = null,
    animationEnd: (() -> Unit)? = null,
    action: (Float) -> Unit
): ValueAnimator {
    val animator = ValueAnimator.ofFloat(first, second).apply {
        duration?.also { this.duration = it }
        addUpdateListener { animation -> action(animation.animatedValue as Float) }
        animationEnd?.also { Handler().postDelayed({ it.invoke() }, duration ?: this.duration) }
        timeInterpolator?.also { interpolator = it }
    }
    animator.start()
    return animator
}
