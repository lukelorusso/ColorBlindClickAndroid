package com.lukelorusso.colorblindclick.presentation.di

import com.lukelorusso.colorblindclick.domain.usecase.base.Logger
import com.lukelorusso.colorblindclick.presentation.error.ErrorMessageFactory
import com.lukelorusso.colorblindclick.presentation.helper.TrackerHelper
import com.lukelorusso.colorblindclick.presentation.helper.impl.TrackerHelperImpl
import com.lukelorusso.colorblindclick.presentation.logger.TimberLogger
import com.lukelorusso.colorblindclick.presentation.ui.capture.CaptureViewModel
import com.lukelorusso.colorblindclick.presentation.ui.error.ErrorMessageFactoryImpl
import com.lukelorusso.colorblindclick.presentation.ui.history.HistoryViewModel
import com.lukelorusso.colorblindclick.presentation.ui.imagepicker.ImagePickerViewModel
import com.lukelorusso.colorblindclick.presentation.ui.info.InfoViewModel
import com.lukelorusso.colorblindclick.presentation.ui.preview.PreviewViewModel
import com.lukelorusso.colorblindclick.presentation.ui.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
val presentationModule = module {
    //region AppComponent
    factory<ErrorMessageFactory> {
        ErrorMessageFactoryImpl(
            get(),
            get()
        )
    }
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
    factory<TrackerHelper> {
        TrackerHelperImpl(
            get()
        )
    }
    //endregion

    //region ViewModel
    viewModel {
        CaptureViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        HistoryViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        InfoViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        PreviewViewModel(
            get(),
            get()
        )
    }
    viewModel {
        SettingsViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        ImagePickerViewModel(
            get(),
            get(),
            get()
        )
    }
    //endregion
}
