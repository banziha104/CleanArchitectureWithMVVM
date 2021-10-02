package com.lyj.cleanarchitecturewithmvvm.domain.repository

import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface LocalTrackRepository : FavoriteDaoContract

interface FavoriteDaoContract{
    fun findAllOnce() : Flowable<List<FavoriteTrackEntity>>
    fun findByTrackId(trackId : Long) : Single<List<FavoriteTrackEntity>>
    fun insertOrDelete(trackEntity: FavoriteTrackEntity) : Completable

    companion object {
        const val LIMIT = 30
    }
}