package com.lyj.cleanarchitecturewithmvvm.data.source.remote.services

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entities.ITunesSearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {
    @GET("search/")
    fun searchITunesList(
        @Query("offset") offset : Int,
        @Query("term") term : String = "greenday",
        @Query("entity") entity : String = "song",
        @Query("limit") limit : Int = LIMIT,
    ) : Single<ITunesSearchResponse.Response>

    companion object{
        const val LIMIT = 30
        const val DEFAULT_OFFSET = 0
    }
}