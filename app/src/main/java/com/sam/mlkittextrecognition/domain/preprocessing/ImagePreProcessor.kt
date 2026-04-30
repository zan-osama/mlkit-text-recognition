package com.sam.mlkittextrecognition.domain.preprocessing

import android.graphics.Bitmap

interface ImagePreProcessor {
    suspend fun process(bitmap: Bitmap): Bitmap
}
