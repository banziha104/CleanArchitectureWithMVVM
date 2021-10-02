package com.lyj.cleanarchitecturewithmvvm.common.extension.android

import androidx.lifecycle.MutableLiveData


val <T> MutableLiveData<T>.unwrappedValue
       get() = this.value ?: throw LiveDataNotInitializedException()

class LiveDataNotInitializedException : Throwable() {

}
