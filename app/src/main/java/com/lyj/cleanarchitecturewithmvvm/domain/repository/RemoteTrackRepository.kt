package com.lyj.cleanarchitecturewithmvvm.domain.repository

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entity.ITunesSearchResponse
import io.reactivex.rxjava3.core.Single

interface RemoteTrackRepository {
    fun requestRemoteTrackData(offSet : Int?) : Single<ITunesSearchResponse.Response>
}