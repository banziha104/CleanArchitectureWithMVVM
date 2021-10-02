package com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.favorite

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.LocalTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainTabType
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    trackPagingRepository: TrackPagingRepository
) : ViewModel() {
    val tabType = MainTabType.FAVORITE

    val pagingAdapterData: Flowable<PagingData<TrackData>> by lazy {
        trackPagingRepository.getPagingData(tabType)
    }
}

class FavoritePagingSource(
    private val localTrackUseCase: LocalTrackUseCase
) : RxPagingSource<Int, TrackData>() {
    companion object {
        private const val DEFAULT_PAGE = 1
    }

    override fun getRefreshKey(state: PagingState<Int, TrackData>): Int? = DEFAULT_PAGE

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, TrackData>> {
        return localTrackUseCase
            .observeFavoriteEntity()
            .single(listOf())
            .map<LoadResult<Int, TrackData>> { list ->
                LoadResult.Page(
                    list,
                    null,
                    null
                )
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }
}
