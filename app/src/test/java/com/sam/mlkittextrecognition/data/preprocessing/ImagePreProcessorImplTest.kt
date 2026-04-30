package com.sam.mlkittextrecognition.data.preprocessing

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.domain.model.ImageProcessingConfig
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class ImagePreProcessorImplTest {

    private lateinit var imagePreProcessor: ImagePreProcessorImpl
    private lateinit var config: ImageProcessingConfig

    @Before
    fun setUp() {
        config = ImageProcessingConfig(
            enhanceContrast = true,
            contrastLevel = 1.5f,
            removeLightBlueBackground = true
        )
        imagePreProcessor = ImagePreProcessorImpl(config)
    }

    @Test
    fun `test remove light blue background`() = runTest {
        val width = 100
        val height = 100
        val testBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        val lightBluePixel = 0xFFB0E0E6.toInt()
        for (x in 0 until width) {
            for (y in 0 until height) {
                testBitmap.setPixel(x, y, lightBluePixel)
            }
        }
        
        val result = imagePreProcessor.process(testBitmap)
        
        assertNotNull(result)
    }

    @Test
    fun `test contrast enhancement`() = runTest {
        config = ImageProcessingConfig(
            enhanceContrast = true,
            contrastLevel = 2.0f,
            removeLightBlueBackground = false
        )
        imagePreProcessor = ImagePreProcessorImpl(config)
        
        val width = 100
        val height = 100
        val testBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        val result = imagePreProcessor.process(testBitmap)
        
        assertNotNull(result)
    }
}
