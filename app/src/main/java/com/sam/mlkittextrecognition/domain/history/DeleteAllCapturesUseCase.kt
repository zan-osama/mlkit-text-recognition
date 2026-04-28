package com.sam.mlkittextrecognition.domain.history

import com.sam.mlkittextrecognition.common.base.usecase.BaseUseCase
import javax.inject.Inject

interface DeleteAllCapturesUseCase : BaseUseCase<Unit, Unit>

class DeleteAllCapturesUseCaseImpl @Inject constructor(
    private val repository: CaptureHistoryRepository
) : DeleteAllCapturesUseCase {
    override suspend operator fun invoke(parameters: Unit) {
        repository.deleteAllCaptures()
    }
}
