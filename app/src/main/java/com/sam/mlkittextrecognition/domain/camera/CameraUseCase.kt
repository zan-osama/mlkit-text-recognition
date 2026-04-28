package com.sam.mlkittextrecognition.domain.camera

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.common.base.usecase.BaseUseCase
import com.sam.mlkittextrecognition.domain.camera.model.CameraResult
import javax.inject.Inject

interface CameraUseCase : BaseUseCase<Bitmap, CameraResult<String>>

class CameraUseCaseImpl @Inject constructor(
    private val cameraRepository: CameraRepository
) : CameraUseCase {
    override suspend operator fun invoke(parameters: Bitmap): CameraResult<String> {
        return cameraRepository.capturePhoto(parameters)
    }
}
