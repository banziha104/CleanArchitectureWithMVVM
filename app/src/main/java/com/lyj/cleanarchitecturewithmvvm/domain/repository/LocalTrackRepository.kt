package com.lyj.cleanarchitecturewithmvvm.domain.repository

import com.lyj.cleanarchitecturewithmvvm.data.source.local.dao.FavoriteDaoEventType
import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface LocalTrackRepository : FavoriteDaoContract

interface FavoriteDaoContract{
    fun findAllOnce() : Single<List<FavoriteTrackEntity>>
    fun findAllContinouse() : Flowable<List<FavoriteTrackEntity>>
    fun findByTrackId(trackId : Long) : Single<List<FavoriteTrackEntity>>
    fun insertOrDelete(trackEntity: FavoriteTrackEntity) :  Single<FavoriteDaoEventType>

    companion object {
        const val LIMIT = 30
    }
}