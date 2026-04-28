package com.sam.mlkittextrecognition.domain.history

import com.sam.mlkittextrecognition.common.base.usecase.BaseUseCase
import com.sam.mlkittextrecognition.domain.model.CaptureHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetAllCapturesUseCase : BaseUseCase<Unit, Flow<List<CaptureHistory>>>

class GetAllCapturesUseCaseImpl @Inject constructor(
    private val repository: CaptureHistoryRepository
) : GetAllCapturesUseCase {
    override suspend operator fun invoke(parameters: Unit): Flow<List<CaptureHistory>> {
        return repository.getAllCaptures()
    }
}
