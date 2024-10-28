package com.lukelorusso.presentation

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lukelorusso.data.di.dataModule
import com.lukelorusso.domain.di.domainModule
import com.lukelorusso.presentation.di.presentationModule
import com.lukelorusso.presentation.logger.CrashlyticsTree
import io.paperdb.Paper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class AndroidApplication : Application() {

    companion object {
        val modules = dataModule +
                domainModule +
                presentationModule
    }

    override fun onCreate() {
        super.onCreate()

        // Init Crashlytics
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = BuildConfig.ENABLE_ANALYTICS

        // Init Debug log
        if (BuildConfig.DEBUG) {
            Timber.plant(CrashlyticsTree())
            Timber.plant(Timber.DebugTree())
        }

        // Init Paper
        Paper.init(this)

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.ERROR)
            }
            androidContext(this@AndroidApplication)
            modules(modules)
        }
    }

}
