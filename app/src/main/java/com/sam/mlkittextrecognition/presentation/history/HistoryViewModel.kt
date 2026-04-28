package com.sam.mlkittextrecognition.presentation.history

import androidx.lifecycle.viewModelScope
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModel
import com.sam.mlkittextrecognition.domain.history.DeleteCaptureUseCase
import com.sam.mlkittextrecognition.domain.history.DeleteAllCapturesUseCase
import com.sam.mlkittextrecognition.domain.history.GetAllCapturesUseCase
import com.sam.mlkittextrecognition.domain.history.SaveCaptureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllCapturesUseCase: GetAllCapturesUseCase,
    private val saveCaptureUseCase: SaveCaptureUseCase,
    private val deleteCaptureUseCase: DeleteCaptureUseCase,
    private val deleteAllCapturesUseCase: DeleteAllCapturesUseCase
) : BaseViewModel<HistoryContract.Event, HistoryContract.State, HistoryContract.Effect>(HistoryContract.State.Idle) {

    override fun processEvent(event: HistoryContract.Event) {
        when (event) {
            is HistoryContract.Event.ViewCreated -> {
                loadCaptures()
            }
            is HistoryContract.Event.DeleteCapture -> {
                deleteCapture(event.id)
            }
            is HistoryContract.Event.DeleteAll -> {
                deleteAllCaptures()
            }
        }
    }

    private fun loadCaptures() {
        viewModelScope.launch {
            getAllCapturesUseCase(Unit).collect { captures ->
                if (captures.isEmpty()) {
                    emitState(HistoryContract.State.Empty)
                } else {
                    emitState(HistoryContract.State.Success(captures))
                }
            }
        }
    }

    fun saveCapture(capture: com.sam.mlkittextrecognition.domain.model.CaptureHistory) {
        viewModelScope.launch {
            saveCaptureUseCase(capture)
        }
    }

    private fun deleteCapture(id: Long) {
        viewModelScope.launch {
            deleteCaptureUseCase(id)
            publishEffect(HistoryContract.Effect.ShowToast("Capture deleted"))
        }
    }

    private fun deleteAllCaptures() {
        viewModelScope.launch {
            deleteAllCapturesUseCase(Unit)
            publishEffect(HistoryContract.Effect.ShowToast("All captures deleted"))
        }
    }
}
