package com.lyj.cleanarchitecturewithmvvm.domain.usecase

import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.repository.TrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.translator.TrackTranslator
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TrackUseCase @Inject constructor(
    private val repository: TrackRepository,
    private val translator: TrackTranslator
) {
    fun execute(checkFavorite: CheckFavorite, offSet: Int?): Single<List<TrackData>> = repository
        .requestTrackData(offSet)
        .map { response ->
            response.results?.filterNotNull()?.map {
                translator.fromResponse(it, checkFavorite)
            } ?: listOf()
        }
}