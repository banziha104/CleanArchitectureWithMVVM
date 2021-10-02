package com.lyj.cleanarchitecturewithmvvm.di.module

import android.content.Context
import androidx.room.Room
import com.lyj.cleanarchitecturewithmvvm.data.source.local.LocalDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDataBase =
        Room.databaseBuilder(
            context, LocalDataBase::class.java,"local.db"
        ).build()
}