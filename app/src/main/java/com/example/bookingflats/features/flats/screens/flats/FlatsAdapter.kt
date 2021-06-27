package com.example.bookingflats.features.flats.screens.flats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingflats.R
import com.example.bookingflats.features.flats.module.view.FlatView
import kotlinx.android.synthetic.main.item_flat_card.view.*

class FlatsAdapter(private val onItemClick: (FlatView) -> Unit) :
    PagingDataAdapter<FlatView, FlatsAdapter.BaseFlatsViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseFlatsViewHolder {
        return FlatsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_flat_card
    }

    abstract class BaseFlatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: FlatView?)
    }

    inner class FlatsViewHolder(itemView: View) : BaseFlatsViewHolder(itemView) {
        override fun bind(item: FlatView?) {
            item?.let {
                with(itemView) {
                    tv_bedroom_value.text = item.bedrooms.toString()
                    tv_name_value.text = item.name
                    tv_distance_value.text = item.distance.toString()
                    button_book.setOnClickListener {
                        onItemClick(item)
                    }
                }
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<FlatView>() {
            override fun areItemsTheSame(
                oldItem: FlatView,
                newItem: FlatView
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: FlatView,
                newItem: FlatView
            ): Boolean = oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseFlatsViewHolder, position: Int) =
        holder.bind(getItem(position))
}

