package com.lyj.cleanarchitecturewithmvvm.data.repository

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entity.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.repository.RemoteTrackRepository
import io.reactivex.rxjava3.core.Single

class RemoteTrackRepositoryImpl (
    private val service: ITunesService
) : RemoteTrackRepository {
    override fun requestRemoteTrackData(
        offSet: Int?
    ): Single<ITunesSearchResponse.Response> = service
        .searchITunesList(offSet ?: ITunesService.DEFAULT_OFFSET)
}