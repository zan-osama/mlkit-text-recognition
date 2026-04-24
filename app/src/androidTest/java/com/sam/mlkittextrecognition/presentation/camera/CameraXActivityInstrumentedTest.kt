package com.sam.mlkittextrecognition.presentation.camera

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraXActivityInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.sam.mlkittextrecognition", appContext.packageName)
    }

    @Test
    fun activity_canBeLaunched() {
        val intent = CameraXActivity.createIntent(InstrumentationRegistry.getInstrumentation().targetContext)
        val scenario = ActivityScenario.launch<CameraXActivity>(intent)

        scenario.onActivity { activity ->
            assertNotNull(activity)
        }

        scenario.close()
    }
}