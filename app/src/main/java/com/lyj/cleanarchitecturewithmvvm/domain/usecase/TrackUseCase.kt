package com.lyj.cleanarchitecturewithmvvm.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.repository.LocalTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.repository.RemoteTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.repository.TrackPagingRepository
import com.lyj.cleanarchitecturewithmvvm.domain.translator.TrackTranslator
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainTabType
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class TrackUseCase @Inject constructor(
    private val localTrackRepository: LocalTrackRepository,
    private val trackPagingRepository: TrackPagingRepository,
    private val translator: TrackTranslator
) {
    fun getPagingTrackData(mainTabType: LiveData<MainTabType>): Flowable<PagingData<TrackData>> =
        trackPagingRepository
            .getPagingTrackData(mainTabType)

    fun insertOrDelete(trackData: TrackData) = localTrackRepository
        .insertOrDelete(FavoriteTrackEntity.fromTrackData(trackData))

    fun observeLocalData(): Flowable<List<TrackData>> = localTrackRepository
        .findAllContinouse()
        .map { list ->
            list.map { translator.fromTrackDataGettable(it) }
        }
}