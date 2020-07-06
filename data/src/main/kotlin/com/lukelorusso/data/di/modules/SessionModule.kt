package com.lukelorusso.data.di.modules

import android.content.Context
import com.lukelorusso.data.di.providers.SessionManager
import com.lukelorusso.data.di.providers.SharedPrefProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Copyright (C) 2020 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 * Dagger module that provides Session class.
 */
@Module
class SessionModule {

    @Provides
    @Singleton
    internal fun provideSharedPrefProvider(context: Context) = SharedPrefProvider(context)

    @Provides
    @Singleton
    internal fun provideSessionManager(sharedPrefProvider: SharedPrefProvider) =
        SessionManager(sharedPrefProvider)

}
