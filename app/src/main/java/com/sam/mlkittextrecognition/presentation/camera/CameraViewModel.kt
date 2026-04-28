package com.sam.mlkittextrecognition.presentation.camera

import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.lifecycle.viewModelScope
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModel
import com.sam.mlkittextrecognition.domain.camera.CameraUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraUseCase: CameraUseCase
) : BaseViewModel<CameraContract.Event, CameraContract.State, CameraContract.Effect>() {

    override val initialState = CameraContract.State.Idle

    private val aspectRatioOptions = listOf(
        AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY to "4:3",
        AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY to "16:9"
    )
    private var currentAspectRatioIndex = 0

    val currentAspectRatio: AspectRatioStrategy
        get() = aspectRatioOptions[currentAspectRatioIndex].first

    val currentAspectRatioLabel: String
        get() = aspectRatioOptions[currentAspectRatioIndex].second

    override fun processEvent(event: CameraContract.Event) {
        when (event) {
            is CameraContract.Event.ViewCreated -> {
                emitState(CameraContract.State.PreviewReady(currentAspectRatioLabel))
            }
            is CameraContract.Event.ToggleAspectRatio -> {
                toggleAspectRatio()
            }
            is CameraContract.Event.TakePhoto -> {
                capturePhoto(event.bitmap)
            }
        }
    }

    private fun toggleAspectRatio() {
        currentAspectRatioIndex = (currentAspectRatioIndex + 1) % aspectRatioOptions.size
        emitState(CameraContract.State.PreviewReady(currentAspectRatioLabel))
        viewModelScope.launch {
            publishEffect(CameraContract.Effect.ShowToast("Switched to $currentAspectRatioLabel"))
        }
    }

    private fun capturePhoto(bitmap: android.graphics.Bitmap) {
        viewModelScope.launch {
            when (val result = cameraUseCase(bitmap)) {
                is com.sam.mlkittextrecognition.domain.camera.model.CameraResult.Success -> {
                    publishEffect(CameraContract.Effect.NavigateToResult(result.data))
                }
                is com.sam.mlkittextrecognition.domain.camera.model.CameraResult.Error -> {
                    publishEffect(CameraContract.Effect.ShowToast(result.message))
                }
            }
        }
    }
}
