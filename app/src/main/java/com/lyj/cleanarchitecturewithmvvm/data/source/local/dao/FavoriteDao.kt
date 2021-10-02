package com.lyj.cleanarchitecturewithmvvm.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lyj.cleanarchitecturewithmvvm.data.source.local.entity.FavoriteTrackEntity
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackId
import com.lyj.cleanarchitecturewithmvvm.domain.repository.LocalTrackRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import java.lang.Exception
import java.lang.NumberFormatException
import java.lang.RuntimeException

@Dao
interface FavoriteDao : LocalTrackRepository {
    @Query("SELECT * FROM favorite_track ORDER BY track_id ASC")
    override fun findAllOnce(): Flowable<List<FavoriteTrackEntity>>

    @Query("SELECT * FROM favorite_track WHERE track_id == :trackId")
    override fun findByTrackId(trackId: Long): Single<List<FavoriteTrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: FavoriteTrackEntity): Completable

    @Query("DELETE FROM favorite_track WHERE id == :id")
    fun delete(id: Long): Completable

    @Throws(NumberFormatException::class)
    override fun insertOrDelete(trackEntity: FavoriteTrackEntity): Completable {
        return try {
            val trackId = trackEntity.trackId?.toLong()
            if (trackId != null) {
                findByTrackId(trackId).flatMapCompletable { list ->
                    if (list.isEmpty()) insert(trackEntity) else delete(trackId)
                }
            } else {
                Completable.error(TrackIdIsNullException())
            }
        } catch (e: Exception) {
            Completable.error(TrackIdIsWrongException(trackId = trackEntity.trackId))
        }
    }
}

class TrackIdIsNullException(
    val msg: String = "Track Id가 null입니다."
) : RuntimeException(msg)

class TrackIdIsWrongException(
    private val trackId: Int?,
    val msg: String = "Track Id가 Long 타입으로 변환할 수 없거나 잘못된 값이 들어왔습니다. Track ID =  $trackId "
) : RuntimeException(msg)