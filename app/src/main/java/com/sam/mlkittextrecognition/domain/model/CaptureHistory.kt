package com.sam.mlkittextrecognition.domain.model

import java.util.Date

data class CaptureHistory(
    val id: Long = 0,
    val imagePath: String,
    val extractedText: String,
    val timestamp: Date = Date()
)
