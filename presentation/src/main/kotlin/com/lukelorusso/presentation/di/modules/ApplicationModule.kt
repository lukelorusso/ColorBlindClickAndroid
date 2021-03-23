package com.lukelorusso.presentation.di.modules

import android.app.Application
import android.content.Context
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.presentation.di.PerApplication
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import dagger.Module
import dagger.Provides

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Dagger module that provides context.
 */
@Module
class ApplicationModule {

    @Provides
    @PerApplication
    internal fun provideContext(application: Application): Context = application.baseContext

    @Provides
    @PerApplication
    internal fun provideErrorMessageFactory(context: Context, logger: Logger): ErrorMessageFactory =
            ErrorMessageFactory.Impl(context, logger)

}
