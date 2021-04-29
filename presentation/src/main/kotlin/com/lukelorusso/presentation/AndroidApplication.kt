package com.lukelorusso.presentation

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.facebook.stetho.Stetho
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lukelorusso.data.di.components.DaggerDataComponent
import com.lukelorusso.data.di.components.DataComponent
import com.lukelorusso.presentation.di.components.ApplicationComponent
import com.lukelorusso.presentation.di.components.DaggerApplicationComponent
import com.lukelorusso.presentation.helper.CrashlyticsTree
import io.paperdb.Paper
import timber.log.Timber

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Android Main Application
 */
class AndroidApplication : Application() {

    @set:VisibleForTesting
    lateinit var appComponent: ApplicationComponent

    @VisibleForTesting
    val dataComponent: DataComponent by lazy { DaggerDataComponent.factory().create(baseContext) }

    override fun onCreate() {
        super.onCreate()

        // Init Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        // Init Stetho
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        // Init Debug log
        if (BuildConfig.DEBUG) {
            Timber.plant(CrashlyticsTree())
            Timber.plant(Timber.DebugTree())
        }

        // Init Paper
        Paper.init(this)

        // Create App Component
        appComponent = createAppComponent()
    }

    @VisibleForTesting
    fun createAppComponent(): ApplicationComponent =
            DaggerApplicationComponent.factory().create(this, dataComponent)

}
