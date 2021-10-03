package com.lyj.cleanarchitecturewithmvvm.presentation.main

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.rxjava3.cachedIn
import com.lyj.cleanarchitecturewithmvvm.R
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.unwrappedValue
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.subscribeOn
import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.LocalTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.RemoteTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val localUseCase: LocalTrackUseCase,
    private val remoteTrackUseCase: RemoteTrackUseCase,
    private val pagingRepository: TrackPagingRepository,
) : AndroidViewModel(application) {

    companion object {
        internal val DEFAULT_TAB_TYPE = MainTabType.LIST
    }

    val currentMainTabType: MutableLiveData<MainTabType> by lazy {
        MutableLiveData<MainTabType>(MainTabType.LIST)
    }

    val currentLocalTrackData: Flowable<List<TrackData>> by lazy {
        localUseCase
            .observeFavoriteEntity()
            .subscribeOn(SchedulerType.IO)
    }

    fun getPagingRepository(mainTabType: MutableLiveData<MainTabType>) = pagingRepository
        .getPagingTrackData(mainTabType)
        .cachedIn(viewModelScope)

    fun insertOrDeleteTrackData(trackData: TrackData) : Completable = localUseCase.insertOrDelete(FavoriteTrackEntity.fromTrackData(trackData))
}


enum class MainTabType(
    @StringRes val titleId: Int,
    val checkFavorite: CheckFavorite,
)  {
    LIST(R.string.main_list_tab_title, CheckFavorite { favoriteTrackIds, currentTrackId ->
        if (currentTrackId != null) {
            currentTrackId in favoriteTrackIds
        } else {
            false
        }
    }),
    FAVORITE(R.string.main_favorite_tab_title, CheckFavorite { _, _ ->
        true
    })
}

