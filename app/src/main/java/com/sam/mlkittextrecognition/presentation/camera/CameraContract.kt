package com.sam.mlkittextrecognition.presentation.camera

import androidx.camera.core.resolutionselector.AspectRatioStrategy
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelEvent
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelState
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelEffect

interface CameraContract {
    sealed interface State : BaseViewModelState {
        object Idle : State
        data class PreviewReady(
            val aspectRatioLabel: String
        ) : State
        data class Error(
            val message: String
        ) : State
    }

    sealed interface Event : BaseViewModelEvent {
        object ViewCreated : Event
        object ToggleAspectRatio : Event
        data class TakePhoto(val bitmap: android.graphics.Bitmap) : Event
    }

    sealed interface Effect : BaseViewModelEffect {
        data class ShowToast(val message: String) : Effect
        data class NavigateToResult(val imagePath: String) : Effect
    }
}
