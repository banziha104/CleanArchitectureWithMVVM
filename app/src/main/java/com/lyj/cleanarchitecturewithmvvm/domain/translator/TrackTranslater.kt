package com.lyj.cleanarchitecturewithmvvm.domain.translator

import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entity.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackDataGettable
import javax.inject.Inject

class TrackTranslator @Inject constructor(){
    fun fromTrackDataGettable(gettable : TrackDataGettable) : TrackData = gettable.run {
            TrackData(
                trackId,
                trackName,
                collectionName,
                artistName,
                url,
            )
    }
}
