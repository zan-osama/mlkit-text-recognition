package com.sam.mlkittextrecognition.data.repository

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.sam.mlkittextrecognition.domain.model.RecognitionResult
import com.sam.mlkittextrecognition.domain.model.TextDomain
import com.sam.mlkittextrecognition.domain.repository.TextRecognitionRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class TextRecognitionRepositoryImpl @Inject constructor(
    private val recognizer: TextRecognizer,
) : TextRecognitionRepository {
    override suspend fun recognizeText(image: Bitmap): RecognitionResult<TextDomain> {
        return suspendCancellableCoroutine { continuation ->
            val inputImage = InputImage.fromBitmap(image, 0)
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    val domainResult = TextRecognitionMapper.map(visionText)
                    continuation.resume(RecognitionResult.Success(domainResult))
                }
                .addOnFailureListener { e ->
                    continuation.resume(RecognitionResult.Error(e.message ?: "Unknown error"))
                }
        }
    }
}