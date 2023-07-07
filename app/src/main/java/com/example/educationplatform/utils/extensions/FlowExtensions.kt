package com.example.educationplatform.utils.extensions

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.launchWhenStarted(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}

fun <T> Flow<T>.doOnFirst(action: suspend (T) -> Unit): Flow<T> = flow {
    var first = false
    collect { value ->
        if (!first) {
            first = true
            action(value)
        }
        emit(value)
    }
}