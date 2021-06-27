package com.example.bookingflats.features.flats.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingflats.R
import com.example.bookingflats.databinding.BaseLoadStateFooterViewItemBinding

class BaseLoadStateViewHolder(
    private val binding: BaseLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): BaseLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_load_state_footer_view_item, parent, false)
            val binding = BaseLoadStateFooterViewItemBinding.bind(view)
            return BaseLoadStateViewHolder(binding, retry)
        }
    }
}
