package com.lyj.cleanarchitecturewithmvvm.domain.translator

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entities.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import javax.inject.Inject

class TrackTranslator @Inject constructor(){
    fun fromResponse(response : ITunesSearchResponse.ResultsItem, checkFavorite: CheckFavorite) : TrackData = response.run {
            TrackData(
                trackId,
                trackName,
                collectionName,
                artistName,
                artworkUrl60,
                checkFavorite
            )
    }

    fun fromEntity(){

    }
}