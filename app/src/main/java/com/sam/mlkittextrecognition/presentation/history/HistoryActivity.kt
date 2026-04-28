package com.sam.mlkittextrecognition.presentation.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sam.mlkittextrecognition.R
import com.sam.mlkittextrecognition.databinding.ActivityHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    private val viewModel: HistoryViewModel by viewModels()
    private var _binding: ActivityHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()

        viewModel.processEvent(HistoryContract.Event.ViewCreated)
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter { capture ->
            // Handle item click - show detail
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    renderState(state)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    handleEffect(effect)
                }
            }
        }
    }

    private fun renderState(state: HistoryContract.State) {
        when (state) {
            is HistoryContract.State.Success -> {
                adapter.submitList(state.captures)
            }
            is HistoryContract.State.Empty -> {
                // Show empty state
            }

            HistoryContract.State.Idle -> {
                // Do nothing
            }
        }
    }

    private fun handleEffect(effect: HistoryContract.Effect) {
        when (effect) {
            is HistoryContract.Effect.ShowToast -> {
                // Show toast with effect.message
            }

            HistoryContract.Effect.ShowLoading -> {
                // Show loading dialog
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, HistoryActivity::class.java)
        }
    }
}
