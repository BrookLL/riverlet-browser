package com.riverlet.browser.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            println("Handle $throwable in CoroutineExceptionHandler")
        }

    protected fun launch(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): DisposableHandle {
        return viewModelScope.launch(coroutineExceptionHandler, start, block)
            .invokeOnCompletion { onScopeOver() }
    }

    protected fun <T> async(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ): DisposableHandle {
        return viewModelScope.async(coroutineExceptionHandler, start, block)
            .invokeOnCompletion { onScopeOver() }
    }

    protected fun onScopeOver() {

    }
}
