package com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.lyj.cleanarchitecturewithmvvm.common.base.PagingAdapterViewModel
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import io.reactivex.rxjava3.core.Observable

class TrackAdapterViewModel(
    override val context: Context,
    override val diffUtil: DiffUtil.ItemCallback<TrackData>,
    val onFavoriteButtonClick: (Observable<Pair<Int,TrackData>>) -> Unit
) : PagingAdapterViewModel<TrackData>
