package com.lyj.benchmark

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.ar.core.Config
import com.lyj.benchmark.test.R
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackDataGettable
import com.lyj.cleanarchitecturewithmvvm.domain.translator.TrackTranslator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Benchmark, which will execute on an Android device.
 *
 * The body of [BenchmarkRule.measureRepeated] is measured in a loop, and Studio will
 * output the result. Modify your code to see how it affects performance.
 */


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ViewBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var translator: TrackTranslator

    fun init() {
        hiltRule.inject()
    }

    @Test
    fun log() {

        benchmarkRule.measureRepeated {
            val data = translator.fromTrackDataGettable(object : TrackDataGettable {
                override val trackId: Int?
                    get() = 1
                override val trackName: String?
                    get() = "a"
                override val collectionName: String?
                    get() = "b"
                override val artistName: String?
                    get() = "c"
                override val url: String?
                    get() = "d"
            })
            Log.d("LogBenchmark", "$data")
        }
    }
}

