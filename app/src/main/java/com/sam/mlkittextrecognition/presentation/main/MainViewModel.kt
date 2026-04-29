package com.sam.mlkittextrecognition.presentation.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModel
import com.sam.mlkittextrecognition.domain.history.SaveCaptureUseCase
import com.sam.mlkittextrecognition.domain.model.CaptureHistory
import com.sam.mlkittextrecognition.domain.model.RecognitionResult
import com.sam.mlkittextrecognition.domain.usecase.RecognizeTextUseCase
import com.sam.mlkittextrecognition.presentation.main.MainActivity.Companion.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recognizeTextUseCase: RecognizeTextUseCase,
    private val saveCaptureUseCase: SaveCaptureUseCase,
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>(MainContract.State.ViewCreated) {

    override fun processEvent(event: MainContract.Event) {
        when (event) {
            MainContract.Event.ViewCreated -> {
                emitState(MainContract.State.ViewCreated)
            }

            is MainContract.Event.DoAnalyzeImage -> {
                viewModelScope.launch {
                    when (val result = recognizeTextUseCase.invoke(Pair(event.inputImage, false))) {
                        is RecognitionResult.Success -> {
                            Log.d(TAG, "success")
                            // Save to history
                            saveCaptureUseCase(
                                CaptureHistory(
                                    imagePath = event.imagePath,
                                    extractedText = result.data.textDomain.text
                                )
                            )
                            emitState(MainContract.State.ShowVisualText(result.data))
                        }

                        is RecognitionResult.Error -> {
                            Log.e(TAG, result.message)
                        }

                        is RecognitionResult.Loading -> {
                            Log.d(TAG, "Processing...")
                        }
                    }
                }
            }

            is MainContract.Event.ToggleTextBlockVisibility -> {
                val currentState = state.value
                if (currentState is MainContract.State.ShowVisualText) {
                    emitState(currentState.copy(showTextBlock = event.show))
                }
            }

            is MainContract.Event.ToggleLinesVisibility -> {
                val currentState = state.value
                if (currentState is MainContract.State.ShowVisualText) {
                    emitState(currentState.copy(showLines = event.show))
                }
            }
        }
    }
}