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

abstract class AViewModel<Data>(private val errorMessageFactory: ErrorMessageFactory) :
    ViewModel() {

    private val liveData: MutableLiveData<Data> by lazy {
        MutableLiveData()
    }
    protected val data = liveData.value

    protected val composite = CompositeDisposable()
    open val router: ARouter? = null

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
        composite.add(Observable.mergeArray(*observables).subscribe { postData(it) })
    }

    protected fun getErrorMessage(error: Throwable): String = errorMessageFactory.getError(error)

    override fun onCleared() {
        super.onCleared()
        router?.clear()
        composite.clear()
    }

}
