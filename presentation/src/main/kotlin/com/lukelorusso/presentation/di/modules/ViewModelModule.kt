package com.lukelorusso.presentation.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukelorusso.presentation.di.ViewModelKey
import com.lukelorusso.presentation.di.factory.ViewModelFactory
import com.lukelorusso.presentation.scenes.camera.CameraViewModel
import com.lukelorusso.presentation.scenes.history.HistoryViewModel
import com.lukelorusso.presentation.scenes.info.InfoViewModel
import com.lukelorusso.presentation.scenes.preview.PreviewDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    //region Add more ViewModels here
    @Binds
    @IntoMap
    @ViewModelKey(InfoViewModel::class)
    internal abstract fun provideInfoViewModel(viewModel: InfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    internal abstract fun provideCameraViewModel(viewModel: CameraViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    internal abstract fun provideHistoryViewModel(viewModel: HistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PreviewDialogViewModel::class)
    internal abstract fun providePreviewDialogViewModel(viewModel: PreviewDialogViewModel): ViewModel
    //endregion
}
