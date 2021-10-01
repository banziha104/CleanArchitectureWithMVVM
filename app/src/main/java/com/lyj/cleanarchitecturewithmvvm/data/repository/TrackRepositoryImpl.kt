package com.lyj.cleanarchitecturewithmvvm.data.repository

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entities.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.repository.TrackRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val service: ITunesService
) : TrackRepository {
    override fun requestTrackData(
        offSet: Int?
    ): Single<ITunesSearchResponse.Response> = service
        .searchITunesList(offSet ?: ITunesService.DEFAULT_OFFSET)
}