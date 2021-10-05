package com.lyj.cleanarchitecturewithmvvm.data.source.remote

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.services.ITunesService
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
@Config(application = HiltTestApplication::class)
class ITunesServiceTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var service : ITunesService

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `검색_서비스_테스트`(){
        service
            .searchITunesList(0)
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertComplete()
            .assertValue {
                println(it.toString())
                it.resultCount != null
            }
    }
}
