package com.sam.mlkittextrecognition.presentation.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.sam.mlkittextrecognition.R
import com.sam.mlkittextrecognition.databinding.ActivityCameraBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraXActivity : AppCompatActivity() {

    private val viewModel: CameraViewModel by viewModels()
    private var _binding: ActivityCameraBinding? = null
    private val binding get() = _binding!!
    private var cameraExecutor: ExecutorService? = null
    private var imageCapture: ImageCapture? = null
    private var currentCameraProvider: ProcessCameraProvider? = null

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"

        fun createIntent(context: Context): Intent {
            return Intent(context, CameraXActivity::class.java)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.processEvent(CameraContract.Event.ViewCreated)
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    renderState(state)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    handleEffect(effect)
                }
            }
        }

        binding.aspectRatioButton.setOnClickListener {
            viewModel.processEvent(CameraContract.Event.ToggleAspectRatio)
        }

        binding.captureButton.setOnClickListener {
            takePhoto()
        }

        if (hasCameraPermission()) {
            viewModel.processEvent(CameraContract.Event.ViewCreated)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun renderState(state: CameraContract.State) {
        when (state) {
            is CameraContract.State.Idle -> {
                // Initial state
            }
            is CameraContract.State.PreviewReady -> {
                binding.aspectRatioButton.text = state.aspectRatioLabel
                startCameraWithRatio(viewModel.currentAspectRatio)
            }
            is CameraContract.State.Error -> {
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleEffect(effect: CameraContract.Effect) {
        when (effect) {
            is CameraContract.Effect.ShowToast -> {
                Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
            }
            is CameraContract.Effect.NavigateToResult -> {
                val intent = Intent().apply {
                    putExtra(EXTRA_IMAGE_PATH, effect.imagePath)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun startCameraWithRatio(aspectRatio: AspectRatioStrategy) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            currentCameraProvider = cameraProvider

            val preview = Preview.Builder()
                .apply {
                    val resolutionSelectorBuilder = ResolutionSelector.Builder().apply {
                        setAspectRatioStrategy(aspectRatio)
                    }

                    this.setResolutionSelector(resolutionSelectorBuilder.build())
                }
                .build()
                .also {
                    it.surfaceProvider = binding.previewView.surfaceProvider
                }

            imageCapture = ImageCapture.Builder()
                .apply {
                    val resolutionSelectorBuilder = ResolutionSelector.Builder().apply {
                        setAspectRatioStrategy(aspectRatio)
                    }

                    this.setResolutionSelector(resolutionSelectorBuilder.build())
                }
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .apply {
                    val resolutionSelectorBuilder = ResolutionSelector.Builder().apply {
                        setAspectRatioStrategy(aspectRatio)
                    }

                    this.setResolutionSelector(resolutionSelectorBuilder.build())
                }
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalysis
                )
            } catch (e: Exception) {
                viewModel.emitState(CameraContract.State.Error("Camera setup failed: ${e.message}"))
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            cacheDir,
            "captured_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor!!,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    runOnUiThread {
                        val intent = Intent().apply {
                            putExtra(EXTRA_IMAGE_PATH, photoFile.absolutePath)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    runOnUiThread {
                        Toast.makeText(
                            this@CameraXActivity,
                            "Capture failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor?.shutdown()
        currentCameraProvider?.unbindAll()
        _binding = null
    }
}