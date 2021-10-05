package com.lyj.cleanarchitecturewithmvvm.presentation.main

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.rxjava3.cachedIn
import com.lyj.cleanarchitecturewithmvvm.R
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.subscribeOn
import com.lyj.cleanarchitecturewithmvvm.data.source.local.dao.FavoriteDaoEventType
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.TrackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val useCase: TrackUseCase,
) : AndroidViewModel(application) {

    val currentMainTabType: MutableLiveData<MainTabType> by lazy {
        MutableLiveData<MainTabType>(MainTabType.LIST)
    }

    val mainDataChangeObserver: PublishSubject<Int> = PublishSubject.create()

    val localDataObserver: Flowable<List<TrackData>> by lazy {
        useCase
            .observeLocalData()
            .subscribeOn(SchedulerType.IO)
    }

    fun getPagingRepository(mainTabType: MutableLiveData<MainTabType>) = useCase
        .getPagingTrackData(mainTabType)
        .cachedIn(viewModelScope)

    fun insertOrDeleteTrackData(trackData: TrackData): Single<FavoriteDaoEventType> =
        useCase
            .insertOrDelete(trackData)
}

enum class MainTabType(
    @StringRes val titleId: Int,
    val checkFavorite: CheckFavorite,
) {
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

