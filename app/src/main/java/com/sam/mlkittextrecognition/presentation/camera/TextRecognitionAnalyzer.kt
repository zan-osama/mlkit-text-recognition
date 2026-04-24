package com.sam.mlkittextrecognition.presentation.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class TextRecognitionAnalyzer : ImageAnalysis.Analyzer {

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {

        }
    }
}