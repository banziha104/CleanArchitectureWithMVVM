package com.lyj.cleanarchitecturewithmvvm

import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entity.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.domain.repository.RemoteTrackRepository
import io.reactivex.rxjava3.core.Single
import org.junit.Test

import org.junit.Assert.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val remoteTrackRepository = mock<RemoteTrackRepository>{
            on { requestRemoteTrackData(0) } doReturn Single.just(ITunesSearchResponse.Response(resultCount = 30))
        }

        val a = remoteTrackRepository.requestRemoteTrackData(0)
        println("마이 ${a.blockingGet().resultCount}")
        assertEquals(4, 2 + 2)
    }
}

