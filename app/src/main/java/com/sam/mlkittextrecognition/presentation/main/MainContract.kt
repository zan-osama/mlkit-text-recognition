package com.sam.mlkittextrecognition.presentation.main

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelEffect
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelEvent
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelState
import com.sam.mlkittextrecognition.domain.model.TextRecognitionData

interface MainContract {

    sealed interface State : BaseViewModelState {
        object ViewCreated : State
        data class ShowVisualText(
            val textRecognitionData: TextRecognitionData,
            val showTextBlock: Boolean = true,
            val showLines: Boolean = true,
        ) : State
    }

    sealed interface Event : BaseViewModelEvent {
        object ViewCreated : Event
        data class DoAnalyzeImage(val inputImage: Bitmap, val imagePath: String) : Event
        data class ToggleTextBlockVisibility(val show: Boolean) : Event
        data class ToggleLinesVisibility(val show: Boolean) : Event
    }

    sealed interface Effect : BaseViewModelEffect {
        object ShowLoader : Effect
        object HideLoader : Effect
        object Kill : Effect
        object ShowProcessing : Effect
    }
}