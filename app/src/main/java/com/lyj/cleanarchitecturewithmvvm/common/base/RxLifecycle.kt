package com.lyj.cleanarchitecturewithmvvm.common.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Predicate
import io.reactivex.rxjava3.subjects.PublishSubject
import org.reactivestreams.Publisher

//
//class RxLifecycleController(
//    private val owner: LifecycleOwner
//) : LifecycleObserver {
//
//    private val publisher : PublishSubject<Lifecycle.Event> = PublishSubject.create()
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
//    fun any(owner : LifecycleOwner, event : Lifecycle.Event){
//        publisher.onNext(event)
//    }
//}
//fun <T> Observable<T>.bindThis() : Observable<T>{
//
//    return this
//}
//class LifecycleTransformer<T>(
//    private val lifecycleEvent: Lifecycle.Event,
//    lifecycleObserver: Observable<Lifecycle.Event>
//) : ObservableTransformer<T, T>,
//    FlowableTransformer<T, T>,
//    SingleTransformer<T, T>,
//    MaybeTransformer<T, T>,
//    CompletableTransformer {
//
//    private val observable = lifecycleObserver.filter { it == lifecycleEvent }
//
//    override fun apply(upstream: Observable<T>): ObservableSource<T> = upstream.takeUntil(observable)
//
//    override fun apply(upstream: Flowable<T>): Publisher<T> = upstream.takeUntil(observable.toFlowable(BackpressureStrategy.LATEST))
//
//    override fun apply(upstream: Single<T>): SingleSource<T> = upstream.takeUntil(observable.firstOrError())
//
//    override fun apply(upstream: Maybe<T>): MaybeSource<T> = upstream.takeUntil(observable.firstElement())
//
//    override fun apply(upstream: Completable): CompletableSource = upstream.ambWith(observable.concatMapCompletable { Completable.complete() })
//
//}