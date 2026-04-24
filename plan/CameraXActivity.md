# Plan: CameraXActivity Implementation

> **Important**: Follow rules in `AGENTS.md` when executing this plan. All code must comply with:
> - MVVM + Clean Architecture
> - StateFlow for UI state (not LiveData)
> - ViewModel for business logic
> - No `lateinit var` - use nullable backing or lazy
> - Feature-based package structure

## Scope

- Camera preview with CameraX PreviewView
- ImageAnalysis for frame processing
- No ML Kit integration (for future)

---

## Step 1: Add CameraX Dependencies

### 1.1 Update `gradle/libs.versions.toml`

Add to `[versions]`:
```toml
camerax = "latest-version"
```

Add to `[libraries]`:
```toml
androidx-camera-core = { group = "androidx.camera", name = "camera-core", version.ref = "camerax" }
androidx-camera-camera2 = { group = "androidx.camera", name = "camera-camera2", version.ref = "camerax" }
androidx-camera-lifecycle = { group = "androidx.camera", name = "camera-lifecycle", version.ref = "camerax" }
androidx-camera-view = { group = "androidx.camera", name = "camera-view", version.ref = "camerax" }
```

### 1.2 Update `app/build.gradle.kts`

Add dependencies:
```kotlin
implementation(libs.androidx.camera.core)
implementation(libs.androidx.camera.camera2)
implementation(libs.androidx.camera.lifecycle)
implementation(libs.androidx.camera.view)
```

---

## Step 2: Add Camera Permission

### 2.1 Update `app/src/main/AndroidManifest.xml`

Add inside `<manifest>` tag (before `<application>`):
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" android:required="true" />
```

---

## Step 3: Create Layout

### 3.1 Create `app/src/main/res/layout/activity_camera.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.camera.view.PreviewView
        android:id="@+id/previewView"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## Step 4: Create CameraXActivity

### 4.1 Create `app/src/main/java/com/sam/mlkittextrecognition/presentation/camera/CameraXActivity.kt`

```kotlin
package com.sam.mlkittextrecognition.presentation.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.sam.mlkittextrecognition.databinding.ActivityCameraBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (hasCameraPermission()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = binding.previewView.surfaceProvider
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        // Process frame here (no ML Kit yet)
                        imageProxy.close()
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                Toast.makeText(this, "Use case binding failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
```

---

## Step 5: Register in Manifest

### 5.1 Update `AndroidManifest.xml`

Add inside `<application>` tag (after MainActivity):
```xml
<activity
    android:name=".presentation.camera.CameraXActivity"
    android:exported="false"
    android:screenOrientation="portrait" />
```

---

## Step 6: Add Navigation (Optional)

### 6.1 Update FirstFragment to navigate to CameraXActivity

Add button click in FirstFragment:
```kotlin
binding.buttonFirst.setOnClickListener {
    startActivity(Intent(this, CameraXActivity::class.java))
}
```

---

## Verification

Run:
```bash
./gradlew assembleDebug
```

Check:
- [ ] Camera preview displays
- [ ] Permission request works
- [ ] Camera flips between front/back (future)

---

## Notes

- Uses `ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST` for backpressure
- Analyzer runs on background thread (not blocking)
- Remember to close ImageProxy after processing
- Feature-based structure: `presentation/camera/`