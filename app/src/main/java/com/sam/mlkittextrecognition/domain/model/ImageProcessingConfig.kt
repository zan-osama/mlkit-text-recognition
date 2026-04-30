package com.sam.mlkittextrecognition.domain.model

data class ImageProcessingConfig(
    val enhanceContrast: Boolean = true,
    val contrastLevel: Float = 1.5f,
    val brightnessAdjustment: Float = 0f,
    val removeLightBlueBackground: Boolean = true,
    val threshold: Int = 128
)
