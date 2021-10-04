package com.lyj.cleanarchitecturewithmvvm.domain.translator

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.lyj.cleanarchitecturewithmvvm.TestConfig.Companion.SDK_VERSION
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackDataGettable
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [SDK_VERSION])
class TrackTranslatorTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var translator: TrackTranslator

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun translatorTests() {
        val artistNameStub = "artistName"
        val trackIdStub = 1
        val collectionNameStub = "collectionName"
        val urlStub = "url"
        val trackNameStub = "trackName"
        val gettable: TrackDataGettable = mock<TrackDataGettable> {
            on { artistName } doReturn "artistName"
            on { trackId } doReturn 1
            on { collectionName } doReturn "collectionName"
            on { url } doReturn "url"
            on { trackName } doReturn "trackName"
        }
        assert(
            translator.fromTrackDataGettable(gettable).run {
                artistName == artistNameStub &&
                        trackId == trackIdStub &&
                        collectionName == collectionNameStub &&
                        url == urlStub &&
                        trackName == trackNameStub
            }
        )
    }
}
