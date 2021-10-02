package com.lyj.cleanarchitecturewithmvvm.di.module

import com.lyj.cleanarchitecturewithmvvm.data.repository.LocalTrackRepositoryImpl
import com.lyj.cleanarchitecturewithmvvm.data.repository.RemoteTrackRepositoryImpl
import com.lyj.cleanarchitecturewithmvvm.data.source.local.LocalDataBase
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.repository.LocalTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.repository.RemoteTrackRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideLocalTrackRepository(
        dataBase: LocalDataBase
    ): LocalTrackRepository = LocalTrackRepositoryImpl(dataBase)


    @Provides
    @ViewModelScoped
    fun provideRemoteTrackRepositoryImpl(
        service: ITunesService
    ): RemoteTrackRepository = RemoteTrackRepositoryImpl(service)
}

