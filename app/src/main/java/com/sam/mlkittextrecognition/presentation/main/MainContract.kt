package com.sam.mlkittextrecognition.presentation.main

import android.graphics.Bitmap
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelEffect
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelEvent
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelState
import com.sam.mlkittextrecognition.domain.model.TextBlockDomain

interface MainContract {

    sealed interface State : BaseViewModelState {
        object ViewCreated : State
        data class ShowVisualText(val textBlocks: List<TextBlockDomain>) : State
    }

    sealed interface Event : BaseViewModelEvent {
        object ViewCreated : Event
        data class DoAnalyzeImage(val inputImage: Bitmap) : Event
    }

    sealed interface Effect : BaseViewModelEffect {
        object ShowLoader : Effect
        object HideLoader : Effect
        object Kill : Effect
    }
}