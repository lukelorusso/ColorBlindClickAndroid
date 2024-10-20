package com.lukelorusso.presentation.helper

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.lukelorusso.presentation.BuildConfig
import timber.log.Timber
import javax.inject.Inject

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class TrackerHelper
@Inject internal constructor(context: Context) {

    object Actions {
        const val DELETED_ITEM = "DELETED_ITEM"
        const val DELETED_ALL_ITEMS = "DELETED_ALL_ITEMS"
        const val GOTO_HOME_PAGE = "GOTO_HOME_PAGE"
        const val GOTO_HELP_PAGE = "GOTO_HELP_PAGE"
        const val GOTO_ABOUT_ME_PAGE = "GOTO_ABOUT_ME_PAGE"
        const val GOTO_SETTINGS = "GOTO_SETTINGS"
        const val SHARED_TEXT = "SHARED_TEXT"
        const val SHARED_PREVIEW = "SHARED_PREVIEW"
        const val PERSISTENCE_EXCEPTION = "PERSISTENCE_EXCEPTION"
    }

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun track(activity: Activity?, action: String) {
        if (isEnable()) {
            Timber.d("TrackerHelper: $activity -> $action")
            activity?.also {
                //firebaseAnalytics.setCurrentScreen(it, action, null)
                firebaseAnalytics.logEvent(
                    FirebaseAnalytics.Event.SCREEN_VIEW,
                    Bundle().apply { putString(FirebaseAnalytics.Param.SCREEN_NAME, action) }
                )
            }
        }
    }

    private fun isEnable(): Boolean = BuildConfig.ENABLE_ANALYTICS

}
