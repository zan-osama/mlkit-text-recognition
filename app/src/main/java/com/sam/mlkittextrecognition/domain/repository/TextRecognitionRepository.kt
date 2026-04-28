package com.sam.mlkittextrecognition.domain.repository

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.domain.model.RecognitionResult
import com.sam.mlkittextrecognition.domain.model.TextRecognitionData

interface TextRecognitionRepository {
    suspend fun recognizeText(image: Bitmap): RecognitionResult<TextRecognitionData>
}