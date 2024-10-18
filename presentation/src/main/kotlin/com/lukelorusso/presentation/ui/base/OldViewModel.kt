package com.lukelorusso.presentation.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
abstract class OldViewModel<Data : Any>(private val errorMessageFactory: ErrorMessageFactory) :
    ViewModel() {

    private val liveData: MutableLiveData<Data> by lazy {
        MutableLiveData()
    }
    protected val data = liveData.value

    private val liveEvent: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }
    private val event = liveEvent.value

    protected val composite = CompositeDisposable()
    open val router: AppRouter? = null

    protected fun postData(data: Data) {
        liveData.postValue(data)
    }

    protected fun postEvent(message: String) {
        liveEvent.postValue(Event(message))
    }

    fun observe(
        owner: LifecycleOwner,
        eventObserver: Observer<in Event<String>>? = null,
        dataObserver: Observer<in Data>
    ) {
        liveData.observe(owner, dataObserver)
        eventObserver?.also { liveEvent.observe(owner, it) }
    }

    fun initRouter(activity: AppCompatActivity, fragment: Fragment?) {
        router?.init(activity, fragment)
    }

    fun subscribe(vararg observables: Observable<Data>) {
        composite.add(Observable.mergeArray(*observables).subscribe { postData(it) })
    }

    protected fun getErrorMessage(error: Throwable): String = errorMessageFactory.getError(error)

    override fun onCleared() {
        super.onCleared()
        router?.clear()
        composite.clear()
    }
}
