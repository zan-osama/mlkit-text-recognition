package com.sam.mlkittextrecognition.domain.history

import com.sam.mlkittextrecognition.domain.model.CaptureHistory
import kotlinx.coroutines.flow.Flow

interface CaptureHistoryRepository {
    suspend fun saveCapture(capture: CaptureHistory)
    fun getAllCaptures(): Flow<List<CaptureHistory>>
    suspend fun deleteCapture(id: Long)
    suspend fun deleteAllCaptures()
}
