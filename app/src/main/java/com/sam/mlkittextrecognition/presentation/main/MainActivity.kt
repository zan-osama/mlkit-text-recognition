package com.sam.mlkittextrecognition.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.sam.mlkittextrecognition.R
import com.sam.mlkittextrecognition.databinding.ActivityMainBinding
import com.sam.mlkittextrecognition.domain.model.TextRecognitionData
import com.sam.mlkittextrecognition.presentation.camera.CameraXActivity
import com.sam.mlkittextrecognition.util.BoundingBoxUtils
import com.sam.mlkittextrecognition.util.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private val viewModel: MainViewModel by viewModels()

    companion object {
        const val TAG = "TextRecognition"
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra(CameraXActivity.Companion.EXTRA_IMAGE_PATH)?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    val bitmap = ImageUtils.rotateBitmapIfRequired(path)
                    binding?.capturedImageView?.setImageBitmap(bitmap)
                    performTextRecognition(bitmap)
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.processEvent(MainContract.Event.ViewCreated)

        doCollectState()
        doCollectEffect()
        setupToggleButtons()

        binding?.openCameraButton?.setOnClickListener {
            checkCameraPermissionAndLaunch()
        }
    }

    private fun performTextRecognition(bitmap: Bitmap) {
        viewModel.processEvent(MainContract.Event.DoAnalyzeImage(bitmap))
    }

    private fun setupToggleButtons() {
        binding?.showTextBlockButton?.setOnClickListener {
            val currentState = viewModel.state.value
            if (currentState is MainContract.State.ShowVisualText) {
                viewModel.processEvent(MainContract.Event.ToggleTextBlockVisibility(!currentState.showTextBlock))
            }
        }

        binding?.showLinesButton?.setOnClickListener {
            val currentState = viewModel.state.value
            if (currentState is MainContract.State.ShowVisualText) {
                viewModel.processEvent(MainContract.Event.ToggleLinesVisibility(!currentState.showLines))
            }
        }
    }

    private fun updateToggleButtons(showTextBlock: Boolean, showLines: Boolean) {
        binding?.showTextBlockButton?.apply {
            text =
                if (showTextBlock) getString(R.string.hide_text_block) else getString(R.string.show_text_block)
            setBackgroundColor(
                if (showTextBlock) ContextCompat.getColor(
                    context,
                    R.color.text_block_color
                ) else ContextCompat.getColor(context, R.color.inactive_color)
            )
        }

        binding?.showLinesButton?.apply {
            text = if (showLines) getString(R.string.hide_lines) else getString(R.string.show_lines)
            setBackgroundColor(
                if (showLines) ContextCompat.getColor(
                    context,
                    R.color.line_color
                ) else ContextCompat.getColor(context, R.color.inactive_color)
            )
        }
    }

    private fun doCollectState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    MainContract.State.ViewCreated -> {
                        binding?.boundingBoxToggleContainer?.visibility = View.GONE
                    }

                    is MainContract.State.ShowVisualText -> {
                        Log.d(
                            TAG,
                            "Text block=${state.textRecognitionData.textDomain.textBlocks.map { it.text }}"
                        )

                        val hasTextBlocks =
                            state.textRecognitionData.textDomain.textBlocks.isNotEmpty()
                        val hasLines =
                            state.textRecognitionData.textDomain.textBlocks.any { it.lines.isNotEmpty() }

                        if (hasTextBlocks || hasLines) {
                            binding?.boundingBoxToggleContainer?.visibility = View.VISIBLE
                            updateToggleButtons(state.showTextBlock, state.showLines)
                            binding?.boundingBoxOverlay?.setVisibility(
                                state.showTextBlock,
                                state.showLines
                            )
                        } else {
                            binding?.boundingBoxToggleContainer?.visibility = View.GONE
                        }

                        handleBoundingBoxesState(state.textRecognitionData)
                    }
                }
            }
        }
    }

    private fun handleBoundingBoxesState(textRecognitionData: TextRecognitionData) {
        val capturedImageView = binding?.capturedImageView ?: return
        val boundingBoxOverlay = binding?.boundingBoxOverlay ?: return

        if (capturedImageView.width > 0 && capturedImageView.height > 0) {
            val displayBounds = BoundingBoxUtils.calculateDisplayBounds(
                imageWidth = textRecognitionData.imageWidth,
                imageHeight = textRecognitionData.imageHeight,
                viewWidth = boundingBoxOverlay.width,
                viewHeight = boundingBoxOverlay.height
            )
            binding?.boundingBoxOverlay?.setData(textRecognitionData, displayBounds)
        } else {
            capturedImageView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        capturedImageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val displayBounds = BoundingBoxUtils.calculateDisplayBounds(
                            imageWidth = textRecognitionData.imageWidth,
                            imageHeight = textRecognitionData.imageHeight,
                            viewWidth = boundingBoxOverlay.width,
                            viewHeight = boundingBoxOverlay.height
                        )
                        binding?.boundingBoxOverlay?.setData(textRecognitionData, displayBounds)
                    }
                }
            )
        }
    }

    private fun doCollectEffect() {
        lifecycleScope.launch {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    MainContract.Effect.HideLoader -> {}
                    MainContract.Effect.Kill -> {}
                    MainContract.Effect.ShowLoader -> {}
                }
            }
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        when {
            hasCameraPermission() -> launchCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun launchCamera() {
        cameraLauncher.launch(CameraXActivity.Companion.createIntent(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}