package com.lyj.cleanarchitecturewithmvvm.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entity.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackDataGettable

@Entity(tableName = "favorite_track")
data class FavoriteTrackEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "track_name")
    override val trackName: String? = null,

    @ColumnInfo(name = "collection_name")
    override val collectionName: String? = null,

    @ColumnInfo(name = "track_id")
    override val trackId: Int? = null,

    @ColumnInfo(name = "artworkUrl60")
    val artworkUrl60: String? = null,

    @ColumnInfo(name = "artist_mame")
    override val artistName: String? = null,
) : TrackDataGettable {
    override var url : String? = artworkUrl60
    companion object{
        fun fromTrackData(data : TrackData) : FavoriteTrackEntity = FavoriteTrackEntity(
            null,
            data.trackName,
            data.collectionName,
            data.trackId,
            data.url,
            data.artistName
        )
    }
}