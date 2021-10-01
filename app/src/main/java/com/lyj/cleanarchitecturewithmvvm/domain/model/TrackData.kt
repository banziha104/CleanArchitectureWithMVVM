package com.lyj.cleanarchitecturewithmvvm.domain.model


typealias TrackId = Int
typealias IsFavorite = Boolean

data class TrackData(
    val trackId : Int?,
    val trackName : String?,
    val collectionName : String?,
    val artistName : String?,
    val url : String?,
    val checkFavorite: CheckFavorite?
)

@JvmInline
value class CheckFavorite(val func: (TrackId?) -> IsFavorite)

interface TrackDataGettable{
    fun getTrackData(checkFavorite: CheckFavorite) : TrackData
}