package com.lyj.cleanarchitecturewithmvvm.common.base

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import io.reactivex.rxjava3.disposables.Disposable

interface DisposeBag : LifecycleOwner {
    val disposeByController: DisposeByController
        get() = getObserver(this)

    companion object {
        private val map: MutableMap<String, DisposeByController> = mutableMapOf()
        private fun getObserver(lifecycleOwner: LifecycleOwner) =
            map[lifecycleOwner::class.java.simpleName] ?: let {
                Log.d(testTag, "${lifecycleOwner::class.java.simpleName}")
                val observer = DisposeByController(lifecycleOwner)
                map[lifecycleOwner::class.java.simpleName] = observer
                observer
            }
    }
}

fun Disposable.disposeBy(disposeBag: DisposeBag,lifecycle: Lifecycle.Event = Lifecycle.Event.ON_DESTROY) = disposeBag.disposeByController.disposeBy(this,lifecycle)

class DisposeByController(
    private val lifecycleOwner: LifecycleOwner
) : LifecycleObserver {

    private val disposableList = mutableListOf<Pair<Disposable, Lifecycle.Event>>()

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun any(owner: LifecycleOwner, event: Lifecycle.Event) {
        disposableList.filter { (_, disposeBy) -> disposeBy == event }
            .forEach { (disposable, _) -> disposable.dispose() }
    }

    fun disposeBy(disposable: Disposable, disposeBy: Lifecycle.Event) =
        disposableList.add(disposable to disposeBy)
}