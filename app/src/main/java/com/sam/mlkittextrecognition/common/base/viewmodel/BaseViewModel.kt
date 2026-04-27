package com.sam.mlkittextrecognition.common.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<EVENT : BaseViewModelEvent, STATE : BaseViewModelState, EFFECT : BaseViewModelEffect> : BaseViewModelContract<EVENT, STATE, EFFECT>, ViewModel() {

    abstract val initialState: STATE

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<STATE> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<EFFECT>()
    override val effect: SharedFlow<EFFECT> = _effect.asSharedFlow()

    protected val currentState: STATE
        get() = _state.value

    override fun emitState(state: STATE) {
        _state.value = state
    }

    override fun publishEffect(effect: EFFECT) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    protected fun launchInScope(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }
}