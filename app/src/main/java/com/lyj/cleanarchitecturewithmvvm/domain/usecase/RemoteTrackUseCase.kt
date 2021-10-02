package com.lyj.cleanarchitecturewithmvvm.domain.usecase

import android.util.Log
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.repository.RemoteTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.translator.TrackTranslator
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RemoteTrackUseCase @Inject constructor(
    private val repository: RemoteTrackRepository,
    private val translator: TrackTranslator
) {
    fun execute(offSet: Int?): Single<List<TrackData>> = repository
        .requestRemoteTrackData(offSet)
        .map { response ->
            response.results?.filterNotNull()?.map {
                translator.fromTrackDataGettable(it)
            } ?: listOf()
        }
}