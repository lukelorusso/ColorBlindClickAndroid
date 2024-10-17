package com.lukelorusso.domain.di

import com.lukelorusso.domain.usecase.*
import org.koin.dsl.module
import com.lukelorusso.domain.usecase.v3.DeleteAllSavedColorsUseCase as DeleteAllSavedColorsUseCaseV3
import com.lukelorusso.domain.usecase.v3.DeleteSavedColorUseCase as DeleteSavedColorUseCaseV3
import com.lukelorusso.domain.usecase.v3.GetAboutMeUrlUseCase as GetAboutMeUrlUseCaseV3
import com.lukelorusso.domain.usecase.v3.GetHelpUrlUseCase as GetHelpUrlUseCaseV3
import com.lukelorusso.domain.usecase.v3.GetHomeUrlUseCase as GetHomeUrlUseCaseV3
import com.lukelorusso.domain.usecase.v3.GetPixelNeighbourhoodUseCase as GetPixelNeighbourhoodUseCaseV3
import com.lukelorusso.domain.usecase.v3.GetSaveCameraOptionsUseCase as GetSaveCameraOptionsUseCaseV3
import com.lukelorusso.domain.usecase.v3.GetSavedColorListUseCase as GetSavedColorListUseCaseV3
import com.lukelorusso.domain.usecase.v3.SetPixelNeighbourhoodUseCase as SetPixelNeighbourhoodUseCaseV3
import com.lukelorusso.domain.usecase.v3.SetSaveCameraOptionsUseCase as SetSaveCameraOptionsUseCaseV3

val domainModule = module {
    //region UseCase
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

    factory { GetPixelNeighbourhoodUseCaseV3(get()) }
    factory { SetPixelNeighbourhoodUseCaseV3(get()) }
    factory { GetSaveCameraOptionsUseCaseV3(get()) }
    factory { SetSaveCameraOptionsUseCaseV3(get()) }

    factory { GetSavedColorListUseCaseV3(get()) }
    factory { DeleteSavedColorUseCaseV3(get()) }
    factory { DeleteAllSavedColorsUseCaseV3(get()) }
    //endregion
}
