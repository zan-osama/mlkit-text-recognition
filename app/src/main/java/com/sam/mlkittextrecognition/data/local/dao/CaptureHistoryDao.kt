package com.sam.mlkittextrecognition.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sam.mlkittextrecognition.data.local.entity.CaptureHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaptureHistoryDao {
    @Insert
    suspend fun insert(captureHistory: CaptureHistoryEntity)

    @Query("SELECT * FROM capture_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<CaptureHistoryEntity>>

    @Query("DELETE FROM capture_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM capture_history")
    suspend fun deleteAll()
}
