package com.sam.mlkittextrecognition.domain.usecase

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.common.base.usecase.BaseUseCase
import com.sam.mlkittextrecognition.domain.model.RecognitionResult
import com.sam.mlkittextrecognition.domain.model.TextDomain
import com.sam.mlkittextrecognition.domain.repository.TextRecognitionRepository
import javax.inject.Inject

interface RecognizeTextUseCase : BaseUseCase<Bitmap, RecognitionResult<TextDomain>>

class RecognizeTextUseCaseImpl @Inject constructor(
    private val repository: TextRecognitionRepository
) : RecognizeTextUseCase {
    override suspend operator fun invoke(parameters: Bitmap): RecognitionResult<TextDomain> {
        return repository.recognizeText(parameters)
    }
}