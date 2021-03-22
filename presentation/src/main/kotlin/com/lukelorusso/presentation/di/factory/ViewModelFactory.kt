package com.lukelorusso.presentation.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukelorusso.presentation.di.PerApplication
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
@PerApplication
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}
