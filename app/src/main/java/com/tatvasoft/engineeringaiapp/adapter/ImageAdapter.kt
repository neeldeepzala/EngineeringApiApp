package com.tatvasoft.engineeringaiapp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(var items: List<String>?) : RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items?.get(position))
    }

    override fun getItemCount(): Int {
        return items?.count() ?: 0
    }
}