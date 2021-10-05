package com.lyj.cleanarchitecturewithmvvm.domain.repository

import com.lyj.cleanarchitecturewithmvvm.TestConfig
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
class LocalTrackRepositoryTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    
    @Inject
    lateinit var localTrackRepository: LocalTrackRepository
    
    @Before
    fun init() {
        hiltRule.inject()
    }

    // LocalDataTest 와 대부분 메소드가 중복, 추후 Repository만 변경되는 경우 테스트
}