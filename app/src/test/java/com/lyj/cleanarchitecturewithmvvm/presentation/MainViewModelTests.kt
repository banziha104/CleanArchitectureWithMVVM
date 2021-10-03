package com.lyj.cleanarchitecturewithmvvm.presentation

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainViewModel
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
@Config(application = HiltTestApplication::class,sdk = [29])
class MainViewModelTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mainViewModel : MainViewModel

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun viewModelTest(){
        mainViewModel
            .currentLocalTrackData
            .test()
            .awaitDone(2, TimeUnit.SECONDS)
            .assertNoErrors()
            .assertValue {
                it.isNotEmpty()
            }
    }
}
