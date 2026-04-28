package com.sam.mlkittextrecognition.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "capture_history")
data class CaptureHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imagePath: String,
    val extractedText: String,
    val timestamp: Date = Date()
)

fun CaptureHistoryEntity.toDomain(): com.sam.mlkittextrecognition.domain.model.CaptureHistory {
    return com.sam.mlkittextrecognition.domain.model.CaptureHistory(
        id = id,
        imagePath = imagePath,
        extractedText = extractedText,
        timestamp = timestamp
    )
}

fun com.sam.mlkittextrecognition.domain.model.CaptureHistory.toEntity(): CaptureHistoryEntity {
    return CaptureHistoryEntity(
        id = id,
        imagePath = imagePath,
        extractedText = extractedText,
        timestamp = timestamp
    )
}
