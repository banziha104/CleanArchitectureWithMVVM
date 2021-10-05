package com.lyj.cleanarchitecturewithmvvm.domain.repository

import androidx.lifecycle.MutableLiveData
import com.lyj.cleanarchitecturewithmvvm.TestConfig
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainTabType
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
@Config(application = HiltTestApplication::class, sdk = [TestConfig.SDK_VERSION])
class RemoteTrackRepositoryTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var remoteTrackRepository: RemoteTrackRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `트랙_데이터_테스트`(){
        remoteTrackRepository
            .requestRemoteTrackData(0)
            .test()
            .awaitDone(2,TimeUnit.SECONDS)
            .assertValue {
                it.resultCount != null && it.resultCount != 0

            }
    }
}