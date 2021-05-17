package com.lukelorusso.presentation.extensions

import android.os.Build
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lukelorusso.presentation.R
import kotlin.math.roundToInt

/**
 * Adds a [Fragment] to this activity's layout.
 * @param containerViewId The container view to where add the fragment.
 * @param fragment The fragment to be added.
 */
fun AppCompatActivity.addFragment(
    containerViewId: Int,
    fragment: Fragment,
    animation: Boolean = false
) {
    supportFragmentManager.beginTransaction().apply {
        if (animation) setCustomAnimations(
            R.anim.transition_enter_from_left,
            R.anim.transition_enter_from_left,
            R.anim.transition_fade_out,
            R.anim.transition_fade_out
        )
        replace(containerViewId, fragment)
    }.commit()
}

fun AppCompatActivity.applyStatusBarMarginTopOnToolbar(
    toolbar: Toolbar,
    extraMarginInDp: Float? = null
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        toolbar.layoutParams = (toolbar.layoutParams as? LinearLayout.LayoutParams)?.apply {
            topMargin = dpToPixel(extraMarginInDp ?: 0F).roundToInt() + getStatusBarHeight()
        }
    }
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}
