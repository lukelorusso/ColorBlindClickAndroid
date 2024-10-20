package com.lukelorusso.presentation.di

import com.lukelorusso.domain.usecase.base.Logger
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.logger.TimberLogger
import com.lukelorusso.presentation.ui.camera.CameraViewModel
import com.lukelorusso.presentation.ui.error.ErrorMessageFactoryImpl
import com.lukelorusso.presentation.ui.history.HistoryViewModel
import com.lukelorusso.presentation.ui.info.InfoViewModel
import com.lukelorusso.presentation.ui.preview.PreviewViewModel
import com.lukelorusso.presentation.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
val presentationModule = module {
    //region AppComponent
    factory<ErrorMessageFactory> { ErrorMessageFactoryImpl(get(), get()) }
    factory<Logger> {
        object : Logger {
            override fun log(message: () -> String) {
                TimberLogger.d(message)
            }

            override fun logError(throwable: () -> Throwable) {
                TimberLogger.e(throwable)
            }
        }
    }
    factory { TrackerHelper(get()) }
    //endregion

    //region ViewModel
    viewModel { CameraViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get(), get(), get()) }
    viewModel { InfoViewModel(get(), get(), get(), get()) }
    viewModel { PreviewViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get()) }
    //endregion
}
