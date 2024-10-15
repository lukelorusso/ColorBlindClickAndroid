package com.lukelorusso.domain.di

import com.lukelorusso.domain.usecase.*
import org.koin.dsl.module
import com.lukelorusso.domain.usecase.v3.GetAboutMeUrlUseCase as GetAboutMeUrlUseCaseV3
import com.lukelorusso.domain.usecase.v3.GetHelpUrlUseCase as GetHelpUrlUseCaseV3
import com.lukelorusso.domain.usecase.v3.GetHomeUrlUseCase as GetHomeUrlUseCaseV3

val domainModule = module {
    //region Repository
    factory { DeleteAllColorsUseCase(get()) }
    factory { DeleteColorUseCase(get()) }
    factory { GetAboutMeUrlUseCase(get()) }
    factory { GetColorUseCase(get()) }
    factory { GetColorListUseCase(get()) }
    factory { GetHelpUrlUseCase(get()) }
    factory { GetHomeUrlUseCase(get()) }
    factory { GetLastLensPositionUseCase(get()) }
    factory { SetLastLensPositionUseCase(get()) }
    factory { GetLastZoomValueUseCase(get()) }
    factory { SetLastZoomValueUseCase(get()) }
    factory { GetPixelNeighbourhoodUseCase(get()) }
    factory { SetPixelNeighbourhoodUseCase(get()) }
    factory { GetSaveCameraOptionsUseCase(get()) }
    factory { SetSaveCameraOptionsUseCase(get()) }

    factory { GetAboutMeUrlUseCaseV3(get()) }
    factory { GetHelpUrlUseCaseV3(get()) }
    factory { GetHomeUrlUseCaseV3(get()) }
    //endregion
}
