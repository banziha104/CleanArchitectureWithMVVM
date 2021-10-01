package com.lyj.cleanarchitecturewithmvvm.domain.repository

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entities.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import io.reactivex.rxjava3.core.Single

interface TrackRepository {
    fun requestTrackData(offSet : Int?) : Single<ITunesSearchResponse.Response>
}