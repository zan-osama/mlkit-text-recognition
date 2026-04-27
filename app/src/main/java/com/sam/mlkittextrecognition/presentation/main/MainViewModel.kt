package com.sam.mlkittextrecognition.presentation.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.sam.mlkittextrecognition.common.base.viewmodel.BaseViewModel
import com.sam.mlkittextrecognition.domain.model.RecognitionResult
import com.sam.mlkittextrecognition.domain.usecase.RecognizeTextUseCase
import com.sam.mlkittextrecognition.presentation.main.MainActivity.Companion.TAG
import com.sam.mlkittextrecognition.presentation.main.MainContract.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recognizeTextUseCase: RecognizeTextUseCase
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    override val initialState: MainContract.State = MainContract.State.ViewCreated

    override fun processEvent(event: MainContract.Event) {
        when (event) {
            MainContract.Event.ViewCreated -> {
                emitState(MainContract.State.ViewCreated)
            }
            is MainContract.Event.DoAnalyzeImage -> {
                viewModelScope.launch {
                    when (val result = recognizeTextUseCase.invoke(event.inputImage)) {
                        is RecognitionResult.Success -> {
                            Log.d(TAG, "success")
                            emitState(MainContract.State.ShowVisualText(result.data.textBlocks))
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
        }
    }
}