package com.sam.mlkittextrecognition.data.repository

import com.sam.mlkittextrecognition.data.local.dao.CaptureHistoryDao
import com.sam.mlkittextrecognition.data.local.entity.CaptureHistoryEntity
import com.sam.mlkittextrecognition.data.local.entity.toDomain
import com.sam.mlkittextrecognition.data.local.entity.toEntity
import com.sam.mlkittextrecognition.domain.history.CaptureHistoryRepository
import com.sam.mlkittextrecognition.domain.model.CaptureHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CaptureHistoryRepositoryImpl @Inject constructor(
    private val dao: CaptureHistoryDao
) : CaptureHistoryRepository {

    override suspend fun saveCapture(capture: CaptureHistory) {
        dao.insert(capture.toEntity())
    }

    override fun getAllCaptures(): Flow<List<CaptureHistory>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteCapture(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun deleteAllCaptures() {
        dao.deleteAll()
    }
}
