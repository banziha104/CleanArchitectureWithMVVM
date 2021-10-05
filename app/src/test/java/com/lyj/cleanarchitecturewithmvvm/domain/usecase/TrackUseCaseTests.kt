package com.lyj.cleanarchitecturewithmvvm.domain.usecase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.lyj.cleanarchitecturewithmvvm.TestConfig
import com.lyj.cleanarchitecturewithmvvm.data.source.local.dao.FavoriteDaoEventType
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
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
class TrackUseCaseTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var trackUseCase: TrackUseCase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `로컬데이터_관찰_테스트`() {
        trackUseCase
            .observeLocalData()
            .test()
            .awaitDone(1, TimeUnit.SECONDS)
            .assertNoErrors()
            .assertValue {
                it.isEmpty()
            }
    }


    @Test
    fun `로컬데이터_저장_테스트`() {
        trackUseCase
            .insertOrDelete(
                TrackData(
                    trackId = 1,
                    collectionName = "collectionName",
                    artistName = "artsistName",
                    isFavorite = true,
                    trackName = "trackName",
                    url = "url"
                )
            )
            .test()
            .awaitDone(1, TimeUnit.SECONDS)
            .assertNoErrors()
            .assertValue {
                it == FavoriteDaoEventType.INSERT
            }
    }

    @Test
    fun `페이징_테스트`(){
        trackUseCase
            .getPagingTrackData(MutableLiveData(MainTabType.LIST))
            .test()
            .awaitDone(2,TimeUnit.SECONDS)
            .assertNoErrors()
    }
}