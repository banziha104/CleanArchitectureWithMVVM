package com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import androidx.paging.rxjava3.flowable
import androidx.recyclerview.widget.DiffUtil
import com.lyj.cleanarchitecturewithmvvm.common.base.PagingAdapterViewModel
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.unwrappedValue
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.subscribeOn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.LocalTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.RemoteTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainTabType
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.lang.NullPointerException
import javax.inject.Inject

class TrackAdapterViewModel(
    override val context: Context,
    override val diffUtil: DiffUtil.ItemCallback<TrackData>,
    val onFavoriteButtonClick: (Observable<Pair<Int,TrackData>>) -> Unit
) : PagingAdapterViewModel<TrackData>


class TrackPagingRepository @Inject constructor(
    private val remoteTrackUseCase: RemoteTrackUseCase,
    private val localTrackUseCase: LocalTrackUseCase
) {
    fun getPagingTrackData(
        mainTabType: LiveData<MainTabType>
    ): Flowable<PagingData<TrackData>> {
        return Pager<Int, TrackData>(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                TrackPagingSource(
                    mainTabType,
                    remoteTrackUseCase,
                    localTrackUseCase
                )
            }
        ).flowable
    }
}

class TrackPagingSource(
    private val mainTabType: LiveData<MainTabType>,
    private val remoteTrackUseCase: RemoteTrackUseCase,
    private val localTrackUseCase: LocalTrackUseCase
) : RxPagingSource<Int, TrackData>() {
    companion object {
        private const val DEFAULT_PAGE = 1
    }

    override fun getRefreshKey(state: PagingState<Int, TrackData>): Int = DEFAULT_PAGE

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, TrackData>> {
        val page = params.key ?: DEFAULT_PAGE

        return when (mainTabType.value!!) {
            MainTabType.LIST -> {
                remoteTrackUseCase
                    .execute(page)
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
                localTrackUseCase
                    .observeFavoriteEntity()
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