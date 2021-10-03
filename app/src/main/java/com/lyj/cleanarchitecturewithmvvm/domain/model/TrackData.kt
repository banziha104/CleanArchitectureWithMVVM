package com.lyj.cleanarchitecturewithmvvm.domain.model

import androidx.recyclerview.widget.DiffUtil
import com.lyj.cleanarchitecturewithmvvm.common.utils.Equatable
import com.lyj.cleanarchitecturewithmvvm.common.utils.IdGettable
import java.lang.NullPointerException


typealias FavoritesTrackIds = List<Int>
typealias TrackId = Int
typealias IsFavorite = Boolean

data class TrackData(
    val trackId: Int?,
    val trackName: String?,
    val collectionName: String?,
    val artistName: String?,
    val url: String?,
    var isFavorite: IsFavorite? = null
) : IdGettable<Int>, Equatable {
    override val id: Int
        get() = trackId ?: throw NullPointerException()
}

@JvmInline
value class CheckFavorite(val func: (FavoritesTrackIds, TrackId?) -> IsFavorite)

interface TrackDataGettable {
    val trackId: Int?
    val trackName: String?
    val collectionName: String?
    val artistName: String?
    val url: String?
}

//data class TrackData(
//    val artworkUrl100: String? = null,
//    val trackTimeMillis: Int? = null,
//    val country: String? = null,
//    val previewUrl: String? = null,
//    val artistId: Int? = null,
//    val trackName: String? = null,
//    val collectionName: String? = null,
//    val artistViewUrl: String? = null,
//    val discNumber: Int? = null,
//    val trackCount: Int? = null,
//    val artworkUrl30: String? = null,
//    val wrapperType: String? = null,
//    val currency: String? = null,
//    val collectionId: Int? = null,
//    val isStreamable: Boolean? = null,
//    val trackExplicitness: String? = null,
//    val collectionViewUrl: String? = null,
//    val trackNumber: Int? = null,
//    val releaseDate: String? = null,
//    val kind: String? = null,
//    val trackId: Int? = null,
//    val collectionPrice: Double? = null,
//    val discCount: Int? = null,
//    val primaryGenreName: String? = null,
//    val trackPrice: Double? = null,
//    val collectionExplicitness: String? = null,
//    val trackViewUrl: String? = null,
//    val artworkUrl60: String? = null,
//    val trackCensoredName: String? = null,
//    val artistName: String? = null,
//    val collectionCensoredName: String? = null,
//    val contentAdvisoryRating: String? = null,
//    val collectionArtistName: String? = null,
//    val collectionArtistId: Int? = null,
//    val collectionArtistViewUrl: String? = null,
//    var isFavorite: IsFavorite? = null
//) : IdGettable<Int>, Equatable {
//    override val id: Int = trackId ?: throw NullPointerException()
//}