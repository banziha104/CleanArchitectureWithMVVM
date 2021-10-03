package com.lyj.cleanarchitecturewithmvvm.presentation.main

import android.app.Application
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import com.lyj.cleanarchitecturewithmvvm.R
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.applyScheduler
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.observeOn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.subscribeOn
import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import com.lyj.cleanarchitecturewithmvvm.domain.model.CheckFavorite
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackId
import com.lyj.cleanarchitecturewithmvvm.domain.usecase.LocalTrackUseCase
import com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.favorite.FavoriteFragment
import com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.list.ListFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import java.lang.RuntimeException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val useCase: LocalTrackUseCase
) : AndroidViewModel(application) {

    companion object {
        internal val DEFAULT_TAB_TYPE = MainTabType.LIST
    }

    val currentMainTabType: MutableLiveData<MainTabType> by lazy {
        MutableLiveData<MainTabType>(MainTabType.LIST)
    }

    val currentLocalTrackData: Flowable<List<TrackData>> by lazy {
        useCase
            .observeFavoriteEntity()
            .subscribeOn(SchedulerType.IO)
    }

    fun insertOrDeleteTrackData(trackData: TrackData) : Completable = useCase.insertOrDelete(FavoriteTrackEntity.fromTrackData(trackData))
}

enum class MainTabType(
    @StringRes val titleId: Int,
    val checkFavorite: CheckFavorite
) : FragmentInstanceGettable {
    LIST(R.string.main_list_tab_title, CheckFavorite { favoriteTrackIds, currentTrackId ->
        if (currentTrackId != null) {
            currentTrackId in favoriteTrackIds
        } else {
            false
        }
    }) {
        override fun getFragment(): Fragment = ListFragment.instance
    },
    FAVORITE(R.string.main_favorite_tab_title, CheckFavorite { _, _ ->
        true
    }) {
        override fun getFragment(): Fragment = FavoriteFragment.instance
    }
}

interface FragmentInstanceGettable {
    fun getFragment(): Fragment
}
