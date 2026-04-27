package com.sam.mlkittextrecognition.domain.repository

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.domain.model.RecognitionResult
import com.sam.mlkittextrecognition.domain.model.TextDomain

interface TextRecognitionRepository {
    suspend fun recognizeText(image: Bitmap): RecognitionResult<TextDomain>
}