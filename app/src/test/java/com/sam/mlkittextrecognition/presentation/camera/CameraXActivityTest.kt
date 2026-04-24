package com.sam.mlkittextrecognition.presentation.camera

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CameraXActivityTest {

    @Test
    fun extraImagePathKey_isCorrect() {
        assertEquals("extra_image_path", CameraXActivity.EXTRA_IMAGE_PATH)
    }

    @Test
    fun createIntent_hasCorrectComponentName() {
        assertNotNull(CameraXActivity::class.java.name)
        assertEquals(
            "com.sam.mlkittextrecognition.presentation.camera.CameraXActivity",
            CameraXActivity::class.java.name
        )
    }
}