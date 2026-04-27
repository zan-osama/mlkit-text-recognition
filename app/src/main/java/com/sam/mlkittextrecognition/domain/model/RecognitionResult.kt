package com.sam.mlkittextrecognition.domain.model

sealed class RecognitionResult<out T> {
    data class Success<T>(val data: T) : RecognitionResult<T>()
    data class Error(val message: String) : RecognitionResult<Nothing>()
    object Loading : RecognitionResult<Nothing>()
}