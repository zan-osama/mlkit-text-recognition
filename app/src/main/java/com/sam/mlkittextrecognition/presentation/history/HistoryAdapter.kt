package com.sam.mlkittextrecognition.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sam.mlkittextrecognition.databinding.ItemHistoryBinding
import com.sam.mlkittextrecognition.domain.model.CaptureHistory
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (CaptureHistory) -> Unit
) : ListAdapter<CaptureHistory, HistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CaptureHistory) {
            binding.apply {
                // Load image using Glide or similar
                // imageView.setImageURI(Uri.fromFile(File(item.imagePath)))
                extractedTextView.text = item.extractedText
                timestampTextView.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(item.timestamp)
                
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CaptureHistory>() {
        override fun areItemsTheSame(oldItem: CaptureHistory, newItem: CaptureHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CaptureHistory, newItem: CaptureHistory): Boolean {
            return oldItem == newItem
        }
    }
}
