package com.sam.mlkittextrecognition.domain.camera.model

sealed class CameraResult<out T> {
    data class Success<T>(val data: T) : CameraResult<T>()
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : CameraResult<Nothing>()
}
