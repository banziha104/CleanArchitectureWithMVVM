package com.lyj.cleanarchitecturewithmvvm.common.extension.android

import android.content.Context
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainTabType
import io.reactivex.rxjava3.core.Observable

fun BottomNavigationView.selectedObserver(context: Context, default : MainTabType? = null) : Observable<MainTabType> = Observable.create { emitter ->
    this.setOnItemSelectedListener { menu ->
        emitter.onNext(MainTabType.values().first { menu.title == context.getString(it.titleId) })
        return@setOnItemSelectedListener true
    }

    if (default != null){
        emitter.onNext(default)
    }
}