package com.lyj.cleanarchitecturewithmvvm.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lyj.cleanarchitecturewithmvvm.data.source.local.dao.FavoriteDao
import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity

@Database(
    entities = [FavoriteTrackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDataBase : RoomDatabase(){
    abstract fun favoriteDao() : FavoriteDao
}