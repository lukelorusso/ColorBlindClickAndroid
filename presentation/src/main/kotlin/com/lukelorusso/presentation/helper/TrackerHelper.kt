package com.lukelorusso.presentation.helper

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.lukelorusso.presentation.BuildConfig
import timber.log.Timber

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class TrackerHelper internal constructor(context: Context) {

    object Action {
        const val DELETED_ITEM = "DELETED_ITEM"
        const val DELETED_ALL_ITEMS = "DELETED_ALL_ITEMS"
        const val GOTO_HOME_PAGE = "GOTO_HOME_PAGE"
        const val GOTO_HELP_PAGE = "GOTO_HELP_PAGE"
        const val GOTO_ABOUT_ME_PAGE = "GOTO_ABOUT_ME_PAGE"
        const val GOTO_SETTINGS = "GOTO_SETTINGS"
        const val GOTO_IMAGE_PICKER = "GOTO_IMAGE_PICKER"
        const val SHARED_TEXT = "SHARED_TEXT"
        const val SHARED_PREVIEW = "SHARED_PREVIEW"
        const val PERSISTENCE_EXCEPTION = "PERSISTENCE_EXCEPTION"
    }

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun track(action: String) {
        if (BuildConfig.ENABLE_ANALYTICS) {
            Timber.d("TrackerHelper -> $action")
            firebaseAnalytics.logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                Bundle().apply { putString(FirebaseAnalytics.Param.SCREEN_NAME, action) }
            )
        }
    }
}
