package com.lukelorusso.presentation.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T : Fragment> T.build(args: Bundle.() -> Unit): T =
    apply { arguments = Bundle().apply(args) }

fun Fragment.isActive() =
    this.isVisible && !this.isDetached
