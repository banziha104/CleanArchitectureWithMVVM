package com.lyj.cleanarchitecturewithmvvm.common.extension.android

import androidx.lifecycle.LiveData


val <T> LiveData<T>.unwrappedValue
       get() = this.value ?: throw LiveDataNotInitializedException()

class LiveDataNotInitializedException : Throwable() {

}
