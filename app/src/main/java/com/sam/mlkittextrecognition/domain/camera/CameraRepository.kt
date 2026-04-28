package com.sam.mlkittextrecognition.domain.camera

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.domain.camera.model.CameraResult

interface CameraRepository {
    suspend fun setupCamera(aspectRatio: Int): CameraResult<Unit>
    suspend fun capturePhoto(bitmap: Bitmap): CameraResult<String>
}
