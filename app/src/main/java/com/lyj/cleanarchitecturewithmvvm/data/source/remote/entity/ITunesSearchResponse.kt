package com.lyj.cleanarchitecturewithmvvm.data.source.remote.entity

import com.google.gson.annotations.SerializedName
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackDataGettable

class ITunesSearchResponse {

    data class Response(
        @field:SerializedName("resultCount")
        val resultCount: Int? = null,

        @field:SerializedName("results")
        val results: List<ResultsItem?>? = null
    )

    data class ResultsItem(

        @field:SerializedName("artworkUrl100")
        val artworkUrl100: String? = null,

        @field:SerializedName("trackTimeMillis")
        val trackTimeMillis: Int? = null,

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("previewUrl")
        val previewUrl: String? = null,

        @field:SerializedName("artistId")
        val artistId: Int? = null,

        @field:SerializedName("trackName")
        override val trackName: String? = null,

        @field:SerializedName("collectionName")
        override val collectionName: String? = null,

        @field:SerializedName("artistViewUrl")
        val artistViewUrl: String? = null,

        @field:SerializedName("discNumber")
        val discNumber: Int? = null,

        @field:SerializedName("trackCount")
        val trackCount: Int? = null,

        @field:SerializedName("artworkUrl30")
        val artworkUrl30: String? = null,

        @field:SerializedName("wrapperType")
        val wrapperType: String? = null,

        @field:SerializedName("currency")
        val currency: String? = null,

        @field:SerializedName("collectionId")
        val collectionId: Int? = null,

        @field:SerializedName("isStreamable")
        val isStreamable: Boolean? = null,

        @field:SerializedName("trackExplicitness")
        val trackExplicitness: String? = null,

        @field:SerializedName("collectionViewUrl")
        val collectionViewUrl: String? = null,

        @field:SerializedName("trackNumber")
        val trackNumber: Int? = null,

        @field:SerializedName("releaseDate")
        val releaseDate: String? = null,

        @field:SerializedName("kind")
        val kind: String? = null,

        @field:SerializedName("trackId")
        override val trackId: Int? = null,

        @field:SerializedName("collectionPrice")
        val collectionPrice: Double? = null,

        @field:SerializedName("discCount")
        val discCount: Int? = null,

        @field:SerializedName("primaryGenreName")
        val primaryGenreName: String? = null,

        @field:SerializedName("trackPrice")
        val trackPrice: Double? = null,

        @field:SerializedName("collectionExplicitness")
        val collectionExplicitness: String? = null,

        @field:SerializedName("trackViewUrl")
        val trackViewUrl: String? = null,

        @field:SerializedName("artworkUrl60")
        val artworkUrl60: String? = null,

        @field:SerializedName("trackCensoredName")
        val trackCensoredName: String? = null,

        @field:SerializedName("artistName")
        override val artistName: String? = null,

        @field:SerializedName("collectionCensoredName")
        val collectionCensoredName: String? = null,

        @field:SerializedName("contentAdvisoryRating")
        val contentAdvisoryRating: String? = null,

        @field:SerializedName("collectionArtistName")
        val collectionArtistName: String? = null,

        @field:SerializedName("collectionArtistId")
        val collectionArtistId: Int? = null,

        @field:SerializedName("collectionArtistViewUrl")
        val collectionArtistViewUrl: String? = null
    ) : TrackDataGettable {
        override val url: String?
            get() = artworkUrl60
    }
}