package com.lukelorusso.presentation.helper.impl

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.lukelorusso.presentation.BuildConfig
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.logger.TimberLogger

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class TrackerHelperImpl internal constructor(context: Context) : TrackerHelper() {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun track(action: String) {
        if (BuildConfig.ENABLE_ANALYTICS) {
            TimberLogger.d { "TrackerHelper -> $action" }
            firebaseAnalytics.logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                Bundle().apply { putString(FirebaseAnalytics.Param.SCREEN_NAME, action) }
            )
        }
    }
}
