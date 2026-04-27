package com.sam.mlkittextrecognition.common.base.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface BaseViewModelContract<EVENT : BaseViewModelEvent, STATE : BaseViewModelState, EFFECT : BaseViewModelEffect> {
    val state: StateFlow<STATE>
    val effect: SharedFlow<EFFECT>

    fun processEvent(event: EVENT)

    fun emitState(state: STATE)

    fun publishEffect(effect: EFFECT)
}

abstract class BaseViewModelExtensions : ViewModel() {
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
}

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch(block = block)
}