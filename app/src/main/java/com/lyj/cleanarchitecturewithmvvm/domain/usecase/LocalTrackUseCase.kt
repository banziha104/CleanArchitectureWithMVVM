package com.lyj.cleanarchitecturewithmvvm.domain.usecase

import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.repository.FavoriteDaoContract
import com.lyj.cleanarchitecturewithmvvm.domain.repository.LocalTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.repository.RemoteTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.translator.TrackTranslator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class LocalTrackUseCase @Inject constructor(
    private val repository: LocalTrackRepository,
    private val translator: TrackTranslator
) {

    fun observeFavoriteEntity(): Flowable<List<TrackData>> =
        repository
            .findAllOnce()
            .onBackpressureBuffer()
            .map { list ->
                list.map { translator.fromTrackDataGettable(it) }
            }

    fun findByTrackId(trackId: Long): Single<TrackData?> =
        repository.findByTrackId(trackId).map<TrackData?> {
            if (it.isEmpty()) {
                translator.fromTrackDataGettable(it.first())
            } else {
                null
            }
        }

    fun insertOrDelete(trackEntity: FavoriteTrackEntity): Completable =
        repository.insertOrDelete(trackEntity)

}