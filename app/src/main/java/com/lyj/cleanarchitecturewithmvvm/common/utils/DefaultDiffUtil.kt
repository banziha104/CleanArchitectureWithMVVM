package com.lyj.cleanarchitecturewithmvvm.common.utils

import androidx.recyclerview.widget.DiffUtil


class DefaultDiffUtil<KEY,ITEM> : DiffUtil.ItemCallback<ITEM>() where ITEM : Equatable, ITEM: IdGettable<KEY> {

    override fun areItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean {
        return oldItem == newItem
    }
}

interface Equatable{
    override fun equals(others : Any?) : Boolean
}

interface IdGettable<T> {
    val id : T
}