package com.lyj.cleanarchitecturewithmvvm.domain.usecase

import com.lyj.cleanarchitecturewithmvvm.domain.repository.RemoteTrackRepository
import com.lyj.cleanarchitecturewithmvvm.domain.translator.TrackTranslator
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackPagingRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [29])
class RemoteTrackUseTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var remoteTrackUseCase: RemoteTrackUseCase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun remoteTrackUseCaseTests(){
        remoteTrackUseCase
            .execute(0)
            .test()
            .awaitDone(1,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                it.isNotEmpty()
            }
    }
}