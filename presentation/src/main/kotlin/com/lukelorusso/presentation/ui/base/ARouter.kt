package com.lukelorusso.presentation.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
abstract class ARouter {

    internal var activity: AppCompatActivity? = null
    internal var fragment: Fragment? = null

    fun init(activity: AppCompatActivity, fragment: Fragment?) {
        this.activity = activity
        this.fragment = fragment
    }

    fun clear() {
        activity = null
        fragment = null
    }

}
