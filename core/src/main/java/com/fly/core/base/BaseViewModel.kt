package com.fly.core.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * MVVM BaseViewModel (ViewModel don't hold View, store and manage UI-related data)
 */
abstract class BaseViewModel : ViewModel() {

    private val composite = CompositeDisposable()

    protected val hasObserver = this.composite.size() > 0

    protected fun addDisposable(d: Disposable?) {
        if (d == null) return
        composite.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }

}