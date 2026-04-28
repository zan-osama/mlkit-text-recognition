package com.sam.mlkittextrecognition.presentation.history

import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelEvent
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelState
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModelEffect
import com.sam.mlkittextrecognition.domain.model.CaptureHistory

interface HistoryContract {
    sealed interface State : BaseViewModelState {
        object Idle : State
        data class Success(
            val captures: List<CaptureHistory>,
        ) : State

        object Empty : State
    }

    sealed interface Event : BaseViewModelEvent {
        object ViewCreated : Event
        data class DeleteCapture(val id: Long) : Event
        object DeleteAll : Event
    }

    sealed interface Effect : BaseViewModelEffect {
        data class ShowToast(val message: String) : Effect
        object ShowLoading : Effect
    }
}
