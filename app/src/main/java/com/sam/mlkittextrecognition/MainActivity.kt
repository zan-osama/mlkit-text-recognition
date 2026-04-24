package com.sam.mlkittextrecognition

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sam.mlkittextrecognition.databinding.ActivityMainBinding
import com.sam.mlkittextrecognition.presentation.camera.CameraXActivity
import com.sam.mlkittextrecognition.util.ImageUtils
import java.io.File

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra(CameraXActivity.EXTRA_IMAGE_PATH)?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    val bitmap = ImageUtils.rotateBitmapIfRequired(path)
                    binding?.capturedImageView?.setImageBitmap(bitmap)
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
        setContentView(binding?.root!!)

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding?.openCameraButton?.setOnClickListener {
            checkCameraPermissionAndLaunch()
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
        cameraLauncher.launch(CameraXActivity.createIntent(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}