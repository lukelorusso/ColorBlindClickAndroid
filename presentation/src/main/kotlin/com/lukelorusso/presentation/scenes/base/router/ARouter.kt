package com.lukelorusso.presentation.scenes.base.router

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

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
