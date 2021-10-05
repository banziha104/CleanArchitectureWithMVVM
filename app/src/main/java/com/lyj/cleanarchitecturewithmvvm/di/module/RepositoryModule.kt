package com.lyj.cleanarchitecturewithmvvm.di.module

import com.lyj.cleanarchitecturewithmvvm.data.repository.LocalTrackRepositoryImpl
import com.lyj.cleanarchitecturewithmvvm.data.repository.RemoteTrackRepositoryImpl
import com.lyj.cleanarchitecturewithmvvm.data.source.local.LocalDataBase
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.domain.repository.LocalTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.repository.RemoteTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.repository.TrackPagingRepository
import com.lyj.cleanarchitecturewithmvvm.domain.repository.TrackPagingRepositoryImpl
import com.lyj.cleanarchitecturewithmvvm.domain.translator.TrackTranslator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideLocalTrackRepository(
        dataBase: LocalDataBase
    ): LocalTrackRepository = LocalTrackRepositoryImpl(dataBase)


    @Provides
    @Singleton
    fun provideRemoteTrackRepositoryImpl(
        service: ITunesService
    ): RemoteTrackRepository = RemoteTrackRepositoryImpl(service)

    @Provides
    @Singleton
    fun providerackPagingRepositoryImpl(
        remote : RemoteTrackRepository,
        local : LocalTrackRepository,
        translator: TrackTranslator
    ): TrackPagingRepository = TrackPagingRepositoryImpl(remote,local,translator)
}

