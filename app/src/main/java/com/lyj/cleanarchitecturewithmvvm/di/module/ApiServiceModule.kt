package com.lyj.cleanarchitecturewithmvvm.di.module

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.ServiceGenerator
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {

    @Provides
    @Singleton
    fun provideITunesService(
        serviceGenerator: ServiceGenerator,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory,
        client: OkHttpClient
    ): ITunesService = serviceGenerator.generateService(
        ITunesService::class.java,
        client,
        callAdapter,
        converter
    )
}