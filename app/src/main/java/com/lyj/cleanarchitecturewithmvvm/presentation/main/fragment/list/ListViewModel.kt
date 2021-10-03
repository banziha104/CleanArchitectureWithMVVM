package com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.list

import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackPagingRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import androidx.paging.rxjava3.cachedIn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.subscribeOn
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.LocalTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.RemoteTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainTabType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    remoteTrackUseCase: RemoteTrackUseCase,
    trackPagingRepository: TrackPagingRepository,
) : ViewModel() {
    val tabType = MainTabType.LIST

    val pagingAdapterData: Flowable<PagingData<TrackData>> by lazy {
        trackPagingRepository.getPagingData(tabType).cachedIn(viewModelScope)
    }
}

class ListPagingSource(
    private val remoteTrackUseCase: RemoteTrackUseCase
) : RxPagingSource<Int, TrackData>() {
    companion object {
        private const val DEFAULT_PAGE = 1
    }

    override fun getRefreshKey(state: PagingState<Int, TrackData>): Int? = DEFAULT_PAGE

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, TrackData>> {
        val page = params.key ?: DEFAULT_PAGE
        return remoteTrackUseCase
            .execute(offSet = page)
            .subscribeOn(SchedulerType.IO)
            .map<LoadResult<Int, TrackData>> { list ->
                LoadResult.Page(
                    list,
                    null,
                    if (list.isEmpty()) null else page + ITunesService.LIMIT
                )
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }
}

