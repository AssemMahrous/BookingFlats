package com.example.bookingflats.features.flats.screens

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class BaseLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<BaseLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: BaseLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BaseLoadStateViewHolder {
        return BaseLoadStateViewHolder.create(parent, retry)
    }
}
