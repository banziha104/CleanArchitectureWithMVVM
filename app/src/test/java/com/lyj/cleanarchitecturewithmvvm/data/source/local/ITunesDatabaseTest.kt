package com.lyj.cleanarchitecturewithmvvm.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.applyScheduler
import com.lyj.cleanarchitecturewithmvvm.data.source.local.dao.FavoriteDao
import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.TimeUnit


@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ITunesDatabaseTest {
    private lateinit var dao: FavoriteDao
    private lateinit var database: LocalDataBase

    @Before
    fun `00_테스트_셋업`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,LocalDataBase::class.java
        ).build()
        dao = database.favoriteDao()
    }

    @Test
    @Throws(Exception::class)
    fun `01_생성_및_조회`() {
        val entity = FavoriteTrackEntity(null, trackId = 1)
        dao
            .insert(entity)
            .applyScheduler(SchedulerType.IO, SchedulerType.IO)
            .andThen(
                dao
                    .findByTrackId(1)
            )
            .test()
            .awaitDone(2, TimeUnit.SECONDS)
            .assertComplete()
            .assertValue {
                it.forEach { println("${it.trackId}") }
                it.size == 1 && it[0].trackId == 1
            }
    }

    @Test
    @Throws(Exception::class)
    fun `02_단일쿼리_테스트`(){
        val entity = FavoriteTrackEntity(null, trackId = 1)
        dao
            .insert(entity)
            .applyScheduler(SchedulerType.IO, SchedulerType.IO)
            .andThen(
                dao
                    .findByTrackId(1)
            )
            .test()
            .awaitDone(2,TimeUnit.SECONDS)
            .assertValue {
                it.isNotEmpty()
            }
    }
    @Test
    @Throws(Exception::class)
    fun `03_삭제`() {
        dao
            .delete(1L)
            .applyScheduler(SchedulerType.IO,SchedulerType.IO)
            .andThen(
                dao
                    .findAllOnce()
            )   .test()
            .awaitDone(2, TimeUnit.SECONDS)
            .assertComplete()
            .assertValue {
                it.isEmpty()
            }
    }

    @Test
    @Throws(Exception::class)
    fun `04_단일쿼리_테스트`(){
        dao
            .findByTrackId(1)
            .applyScheduler(SchedulerType.IO, SchedulerType.IO)
            .test()
            .awaitDone(2,TimeUnit.SECONDS)
            .assertValue {
                it.isEmpty()
            }
    }

    @After
    fun `99_테스트_완료처리`() {
        database.close()
    }
}