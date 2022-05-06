package com.someparking.androidapp.core.base.mvvm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*

fun <T> MutableLiveData<T>.onNext(next: T) {
    this.value = next
}

inline fun <reified T : Any> LiveData<T>.requireValue(): T {
    return requireNotNull(this.value)
}

inline fun <reified T : Any> MutableLiveData<T>.update(
    update: (T) -> T
) {
    value = update.invoke(requireValue())
}

inline fun <reified T : Any> MutableLiveData<T>.postUpdate(
    update: (T) -> T
) {
    postValue(update.invoke(requireValue()))
}

@Deprecated("Лучше использовать расширение-делегат Fragment.viewModels(), который объединяет создание фабрики VM и обращение к провайдеру VM в одном методе")
inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
    }
}

@Deprecated("Лучше использовать расширение-делегат Fragment.viewModels(), который объединяет создание фабрики VM и обращение к провайдеру VM в одном методе")
inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, viewModelFactory)[T::class.java]
}

inline fun <reified VM : ViewModel> Fragment.viewModels(
    crossinline viewModelProducer: () -> VM
): Lazy<VM> {
    return viewModels(this, viewModelProducer)
}

inline fun <reified VM : ViewModel> FragmentActivity.viewModels(
    crossinline viewModelProducer: () -> VM
): Lazy<VM> {
    return viewModels(this, viewModelProducer)
}

inline fun <reified VM : ViewModel> viewModels(
    viewModelStoreOwner: ViewModelStoreOwner,
    crossinline viewModelProducer: () -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(modelClass: Class<VM>) = viewModelProducer() as VM
        }
        val viewModelProvider = ViewModelProvider(viewModelStoreOwner, factory)
        viewModelProvider[VM::class.java]
    }
}

// Why use getViewLifecycleOwner? See https://github.com/android/architecture-components-samples/issues/47
inline fun <reified T : Any, reified L : LiveData<T>> Fragment.observe(
    liveData: L,
    noinline block: (T) -> Unit
) {
    liveData.observe(this.viewLifecycleOwner, Observer { it?.let { block.invoke(it) } })
}

inline fun <reified T : Any, reified L : LiveData<T>> FragmentActivity.observe(
    liveData: L,
    noinline block: (T) -> Unit
) {
    liveData.observe(this, Observer { it?.let { block.invoke(it) } })
}

// Why use getViewLifecycleOwner? See https://github.com/android/architecture-components-samples/issues/47
inline fun <reified T : Any, reified L : CommandsLiveData<T>> Fragment.observe(
    liveData: L,
    noinline block: (T) -> Unit
) {
    observe(this.viewLifecycleOwner, liveData, block)
}

inline fun <reified T : Any, reified L : CommandsLiveData<T>> FragmentActivity.observe(
    liveData: L,
    noinline block: (T) -> Unit
) {
    observe(this, liveData, block)
}

inline fun <reified T : Any, reified L : CommandsLiveData<T>> observe(
    lifecycleOwner: LifecycleOwner,
    liveData: L,
    noinline block: (T) -> Unit
) {
    liveData.observe(lifecycleOwner, Observer<LinkedList<T>> { commands ->
        if (commands == null) {
            return@Observer
        }
        var command: T?
        do {
            command = commands.poll()
            if (command != null) {
                block.invoke(command)
            }
        } while (command != null)
    })
}
