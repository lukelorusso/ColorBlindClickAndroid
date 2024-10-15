package com.lukelorusso.presentation.ui.v3.base

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
abstract class AppRouter {

    internal var activity: Activity? = null
    internal var fragment: Fragment? = null

    fun init(activity: Activity, fragment: Fragment?) {
        this.activity = activity
        this.fragment = fragment
    }

    fun clear() {
        activity = null
        fragment = null
    }

}
