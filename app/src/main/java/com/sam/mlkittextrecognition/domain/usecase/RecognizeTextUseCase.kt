package com.sam.mlkittextrecognition.domain.usecase

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.common.base.usecase.BaseUseCase
import com.sam.mlkittextrecognition.domain.model.RecognitionResult
import com.sam.mlkittextrecognition.domain.model.TextRecognitionData
import com.sam.mlkittextrecognition.domain.preprocessing.ImagePreProcessor
import com.sam.mlkittextrecognition.domain.repository.TextRecognitionRepository
import javax.inject.Inject

interface RecognizeTextUseCase :
    BaseUseCase<Pair<Bitmap, Boolean>, RecognitionResult<TextRecognitionData>>

class RecognizeTextUseCaseImpl @Inject constructor(
    private val repository: TextRecognitionRepository,
    private val imagePreProcessor: ImagePreProcessor,
) : RecognizeTextUseCase {
    override suspend operator fun invoke(parameters: Pair<Bitmap, Boolean>): RecognitionResult<TextRecognitionData> {
        val processedBitmap =
            if (parameters.second) imagePreProcessor.process(parameters.first) else parameters.first
        return repository.recognizeText(processedBitmap)
    }
}