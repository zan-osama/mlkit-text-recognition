package com.sam.mlkittextrecognition.data.repository

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.sam.mlkittextrecognition.common.base.usecase.BaseUseCase
import com.sam.mlkittextrecognition.domain.model.RecognitionResult
import com.sam.mlkittextrecognition.domain.repository.TextRecognitionRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class TextRecognitionRepositoryImpl @Inject constructor(
    private val recognizer: TextRecognizer,
) : TextRecognitionRepository {
    override suspend fun recognizeText(image: Bitmap): RecognitionResult<Text> {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(image, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->

                    continuation.resume(RecognitionResult.Success(visionText))
                }
                .addOnFailureListener { e ->
                    continuation.resume(RecognitionResult.Error(e.message ?: "Unknown error"))
                }
        }
    }
}