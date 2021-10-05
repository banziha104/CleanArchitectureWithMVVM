package com.lyj.cleanarchitecturewithmvvm.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import androidx.paging.rxjava3.flowable
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.subscribeOn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.translator.TrackTranslator
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainTabType
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface TrackPagingRepository{
    fun getPagingTrackData(
        mainTabType: LiveData<MainTabType>
    ): Flowable<PagingData<TrackData>>
}

class TrackPagingRepositoryImpl @Inject constructor(
    private val remoteTrackRepository: RemoteTrackRepository,
    private val localTrackRepository: LocalTrackRepository,
    private val translator: TrackTranslator,
) : TrackPagingRepository{
    override fun getPagingTrackData(
        mainTabType: LiveData<MainTabType>
    ): Flowable<PagingData<TrackData>> {
        return Pager<Int, TrackData>(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                TrackPagingSource(
                    mainTabType,
                    remoteTrackRepository,
                    localTrackRepository,
                    translator
                )
            }
        ).flowable
    }
}

class TrackPagingSource(
    private val mainTabType: LiveData<MainTabType>,
    private val remoteTrackRepository: RemoteTrackRepository,
    private val localTrackRepository: LocalTrackRepository,
    private val translator: TrackTranslator
) : RxPagingSource<Int, TrackData>() {
    companion object {
        private const val DEFAULT_PAGE = 1
    }

    override fun getRefreshKey(state: PagingState<Int, TrackData>): Int = DEFAULT_PAGE

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, TrackData>> {
        val page = params.key ?: DEFAULT_PAGE

        return when (mainTabType.value!!) {
            MainTabType.LIST -> {
                remoteTrackRepository
                    .requestRemoteTrackData(page)
                    .map { response ->
                        response.results?.filterNotNull()?.map {
                            translator.fromTrackDataGettable(it)
                        } ?: listOf()
                    }
                    .subscribeOn(SchedulerType.IO)
                    .map<LoadResult<Int, TrackData>> { list ->
                        LoadResult.Page(
                            list,
                            null,
                            if (list.isNotEmpty()) page + ITunesService.LIMIT else null,
                        )
                    }
            }
            MainTabType.FAVORITE -> {
                localTrackRepository
                    .findAllContinouse()
                    .onBackpressureBuffer()
                    .map { list ->
                        list.map { translator.fromTrackDataGettable(it) }
                    }
                    .subscribeOn(SchedulerType.IO)
                    .firstOrError()
                    .map<LoadResult<Int, TrackData>> { list ->
                        Log.d(testTag,"재 호출")
                        LoadResult.Page(
                            list,
                            null,
                            null,
                        )
                    }
            }
        }.onErrorReturn {
            LoadResult.Error(it)
        }
    }
}