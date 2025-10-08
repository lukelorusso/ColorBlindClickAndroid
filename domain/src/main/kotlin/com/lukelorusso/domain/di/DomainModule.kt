package com.lukelorusso.domain.di

import com.lukelorusso.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    //region UseCase
    factory { DecodeColorHexUseCase(get(), get(), get()) }

    factory { GetAboutMeUrlUseCase(get()) }
    factory { GetHelpUrlUseCase(get(), get()) }
    factory { GetHomeUrlUseCase(get(), get()) }

    factory { GetPixelNeighbourhoodUseCase(get()) }
    factory { SetPixelNeighbourhoodUseCase(get()) }
    factory { GetSaveCameraOptionsUseCase(get()) }
    factory { SetSaveCameraOptionsUseCase(get()) }
    factory { GetLastLensPositionUseCase(get()) }
    factory { SetLastLensPositionUseCase(get()) }
    factory { GetLastZoomValueUseCase(get()) }
    factory { SetLastZoomValueUseCase(get()) }

    factory { GetSavedColorListUseCase(get()) }
    factory { DeleteSavedColorUseCase(get()) }
    factory { DeleteAllSavedColorsUseCase(get()) }
    //endregion
}
