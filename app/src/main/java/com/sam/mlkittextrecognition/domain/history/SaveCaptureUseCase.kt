package com.sam.mlkittextrecognition.domain.history

import com.sam.mlkittextrecognition.common.base.usecase.BaseUseCase
import com.sam.mlkittextrecognition.domain.model.CaptureHistory
import javax.inject.Inject

interface SaveCaptureUseCase : BaseUseCase<CaptureHistory, Unit>

class SaveCaptureUseCaseImpl @Inject constructor(
    private val repository: CaptureHistoryRepository
) : SaveCaptureUseCase {
    override suspend operator fun invoke(parameters: CaptureHistory) {
        repository.saveCapture(parameters)
    }
}
