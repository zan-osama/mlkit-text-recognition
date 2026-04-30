package com.sam.mlkittextrecognition.data.preprocessing

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.sam.mlkittextrecognition.domain.preprocessing.ImagePreProcessor
import com.sam.mlkittextrecognition.domain.model.ImageProcessingConfig
import javax.inject.Inject

class ImagePreProcessorImpl @Inject constructor(
    private val config: ImageProcessingConfig
) : ImagePreProcessor {

    override suspend fun process(bitmap: Bitmap): Bitmap {
        return if (config.removeLightBlueBackground) {
            removeLightBlueBackground(bitmap)
        } else {
            enhanceContrast(bitmap)
        }
    }

    private fun removeLightBlueBackground(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val processedBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888)
        
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF
            
            if (b > 180 && g > 180 && b > r + 30) {
                val gray = (r * 0.299 + g * 0.587 + b * 0.114).toInt()
                pixels[i] = if (gray > 200) {
                    0xFFFFFFFF.toInt()
                } else {
                    0xFF000000.toInt()
                }
            }
        }
        
        processedBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return enhanceContrast(processedBitmap)
    }

    private fun enhanceContrast(bitmap: Bitmap): Bitmap {
        val contrast = config.contrastLevel
        
        val cm = ColorMatrix().apply {
            set(floatArrayOf(
                contrast, 0f, 0f, 0f, -(0.5f * contrast) + 0.5f,
                0f, contrast, 0f, 0f, -(0.5f * contrast) + 0.5f,
                0f, 0f, contrast, 0f, -(0.5f * contrast) + 0.5f,
                0f, 0f, 0f, 1f, 0f
            ))
        }
        
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(cm)
        }
        
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        
        return result
    }
}
