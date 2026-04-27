package com.sam.mlkittextrecognition.domain.repository

import android.graphics.Bitmap
import com.google.mlkit.vision.text.Text
import com.sam.mlkittextrecognition.domain.model.RecognitionResult

interface TextRecognitionRepository {
    suspend fun recognizeText(image: Bitmap): RecognitionResult<Text>
}