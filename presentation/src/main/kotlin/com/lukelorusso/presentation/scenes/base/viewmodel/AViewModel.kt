package com.lukelorusso.presentation.scenes.base.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.router.ARouter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Abstract class representing a Presenter in a model view presenter (MVI) pattern.
 */
abstract class AViewModel<Data>(
        private val errorMessageFactory: ErrorMessageFactory? = null,
        private val router: ARouter? = null
) : ViewModel() {

    private val liveData: MutableLiveData<Data> by lazy {
        MutableLiveData()
    }
    protected val data = liveData.value
    protected val composite = CompositeDisposable()

    @set:VisibleForTesting
    var testMode: Boolean = false

    protected fun postData(data: Data) {
        liveData.postValue(data)
    }

    fun initRouter(activity: AppCompatActivity, fragment: Fragment?) {
        router?.init(activity, fragment)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in Data>) {
        liveData.observe(owner, observer)
    }

    fun subscribe(vararg observables: Observable<Data>) {
        if (!testMode) {
            composite.add(Observable.mergeArray(*observables).subscribe { postData(it) })
        }
    }

    protected fun getErrorMessage(error: Throwable): String =
        errorMessageFactory?.getError(error) ?: error.message ?: ""

    override fun onCleared() {
        super.onCleared()
        router?.clear()
        composite.clear()
    }

}
