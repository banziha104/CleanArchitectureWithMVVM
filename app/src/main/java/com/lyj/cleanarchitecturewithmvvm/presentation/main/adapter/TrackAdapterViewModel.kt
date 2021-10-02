package com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import androidx.paging.rxjava3.flowable
import androidx.recyclerview.widget.DiffUtil
import com.lyj.cleanarchitecturewithmvvm.common.base.PagingAdapterViewModel
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.subscribeOn
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.LocalTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.RemoteTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainTabType
import com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.favorite.FavoritePagingSource
import com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.list.ListPagingSource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TrackAdapterViewModel(
    override val context: Context,
    override val diffUtil: DiffUtil.ItemCallback<TrackData>,
    val onFavoriteButtonClick : (Observable<TrackData>) -> Unit
) : PagingAdapterViewModel<TrackData>


class TrackPagingRepository @Inject constructor(
    private val remoteTrackUseCase: RemoteTrackUseCase,
    private val localTrackUseCase: LocalTrackUseCase
) {

    fun getPagingData(
        mainTabType: MainTabType,
    ): Flowable<PagingData<TrackData>> = when (mainTabType) {
        MainTabType.LIST -> getListPagingData()
        MainTabType.FAVORITE -> getFavoritePagingData()
    }

    private fun getListPagingData(
    ): Flowable<PagingData<TrackData>> {
        return Pager<Int, TrackData>(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { ListPagingSource(remoteTrackUseCase) }
        ).flowable
    }

    private fun getFavoritePagingData(): Flowable<PagingData<TrackData>> {
        return Pager<Int, TrackData>(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { FavoritePagingSource(localTrackUseCase) }
        ).flowable
    }
}