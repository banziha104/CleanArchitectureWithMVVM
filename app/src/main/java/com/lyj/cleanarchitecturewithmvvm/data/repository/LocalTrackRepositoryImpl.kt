package com.lyj.cleanarchitecturewithmvvm.data.repository

import com.lyj.cleanarchitecturewithmvvm.data.source.local.LocalDataBase
import com.lyj.cleanarchitecturewithmvvm.domain.repository.FavoriteDaoContract
import com.lyj.cleanarchitecturewithmvvm.domain.repository.LocalTrackRepository

class LocalTrackRepositoryImpl (
    private val database : LocalDataBase
) : LocalTrackRepository, FavoriteDaoContract by database.favoriteDao()