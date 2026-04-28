package com.sam.mlkittextrecognition.data.repository

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.domain.camera.CameraRepository
import com.sam.mlkittextrecognition.domain.camera.model.CameraResult
import javax.inject.Inject

class CameraRepositoryImpl @Inject constructor() : CameraRepository {

    override suspend fun setupCamera(aspectRatio: Int): CameraResult<Unit> {
        return try {
            CameraResult.Success(Unit)
        } catch (e: Exception) {
            CameraResult.Error(e.message ?: "Camera setup failed", e)
        }
    }

    override suspend fun capturePhoto(bitmap: Bitmap): CameraResult<String> {
        return try {
            CameraResult.Success("image_path_here")
        } catch (e: Exception) {
            CameraResult.Error(e.message ?: "Capture failed", e)
        }
    }
}
