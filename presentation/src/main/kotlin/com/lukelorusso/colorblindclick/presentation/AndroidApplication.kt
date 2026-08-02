package com.lukelorusso.colorblindclick.presentation

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lukelorusso.colorblindclick.data.di.dataModule
import com.lukelorusso.colorblindclick.domain.di.domainModule
import com.lukelorusso.colorblindclick.presentation.di.presentationModule
import com.lukelorusso.colorblindclick.presentation.logger.CrashlyticsTree
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

        // Init logging trees
        if (BuildConfig.ENABLE_ANALYTICS) {
            Timber.plant(CrashlyticsTree())
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.ERROR)
            }
            androidContext(this@AndroidApplication)
            modules(modules)
        }
    }

}
