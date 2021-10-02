package com.lyj.cleanarchitecturewithmvvm.common.extension.lang

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T> Observable<T>.applyScheduler(
    subscribeOn: SchedulerType,
    observeOn: SchedulerType
): Observable<T> =
    this.subscribeOn(subscribeOn.scheduler).observeOn(observeOn.scheduler)

fun <T> Single<T>.applyScheduler(subscribeOn: SchedulerType, observeOn: SchedulerType): Single<T> =
    this.subscribeOn(subscribeOn.scheduler).observeOn(observeOn.scheduler)


fun <T> Flowable<T>.applyScheduler(
    subscribeOn: SchedulerType,
    observeOn: SchedulerType
): Flowable<T> =
    this.subscribeOn(subscribeOn.scheduler).observeOn(observeOn.scheduler)

fun <T> Maybe<T>.applyScheduler(subscribeOn: SchedulerType, observeOn: SchedulerType): Maybe<T> =
    this.subscribeOn(subscribeOn.scheduler).observeOn(observeOn.scheduler)

fun Completable.applyScheduler(subscribeOn: SchedulerType, observeOn: SchedulerType): Completable =
    this.subscribeOn(subscribeOn.scheduler).observeOn(observeOn.scheduler)


fun <T> Observable<T>.observeOn(observeOn: SchedulerType): Observable<T> =
    this.observeOn(observeOn.scheduler)

fun <T> Single<T>.observeOn(observeOn: SchedulerType): Single<T> =
    this.observeOn(observeOn.scheduler)


fun <T> Flowable<T>.observeOn(observeOn: SchedulerType): Flowable<T> =
    this.observeOn(observeOn.scheduler)

fun <T> Maybe<T>.observeOn(observeOn: SchedulerType): Maybe<T> =
    this.observeOn(observeOn.scheduler)

fun Completable.observeOn(observeOn: SchedulerType): Completable =
    this.observeOn(observeOn.scheduler)


fun <T> Observable<T>.subscribeOn(subscribeOn: SchedulerType): Observable<T> =
    this.subscribeOn(subscribeOn.scheduler)

fun <T> Single<T>.subscribeOn(subscribeOn: SchedulerType): Single<T> =
    this.subscribeOn(subscribeOn.scheduler)


fun <T> Flowable<T>.subscribeOn(subscribeOn: SchedulerType): Flowable<T> =
    this.subscribeOn(subscribeOn.scheduler)

fun <T> Maybe<T>.subscribeOn(subscribeOn: SchedulerType): Maybe<T> =
    this.subscribeOn(subscribeOn.scheduler)

fun Completable.subscribeOn(subscribeOn: SchedulerType): Completable =
    this.subscribeOn(subscribeOn.scheduler)

enum class SchedulerType(
    val scheduler: Scheduler
) {
    MAIN(AndroidSchedulers.mainThread()),
    IO(Schedulers.io()),
    NEW(Schedulers.newThread()),
    TRAMPOLIN(Schedulers.trampoline()),
    COMPUTATION(Schedulers.computation())
}