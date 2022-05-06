package com.someparking.androidapp.core.base.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<State : ViewState>(
    val initialViewState: State? = null
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    protected val _viewState = MutableLiveData<State>()
        .apply {
            initialViewState?.let { value = it }
        }

    protected val _viewCommands = MutableCommandsLiveData<ViewCommand>()

    val viewState: LiveData<State> = _viewState

    val viewCommands: CommandsLiveData<ViewCommand> = _viewCommands

    public override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    protected fun Disposable.safeSubscribe(): Disposable {
        compositeDisposable.add(this)
        return this
    }

    protected fun requireViewState(): State {
        return requireNotNull(viewState.value)
    }
}