package com.sam.mlkittextrecognition.domain.history

import com.sam.mlkittextrecognition.common.base.usecase.BaseUseCase
import javax.inject.Inject

interface DeleteCaptureUseCase : BaseUseCase<Long, Unit>

class DeleteCaptureUseCaseImpl @Inject constructor(
    private val repository: CaptureHistoryRepository
) : DeleteCaptureUseCase {
    override suspend operator fun invoke(parameters: Long) {
        repository.deleteCapture(parameters)
    }
}
