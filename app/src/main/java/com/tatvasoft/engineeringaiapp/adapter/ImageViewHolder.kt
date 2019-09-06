package com.tatvasoft.engineeringaiapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tatvasoft.engineeringaiapp.R
import com.tatvasoft.engineeringaiapp.util.GlideApp

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val ivImageItem: ImageView = view.findViewById(R.id.ivItemImage)

    fun bind(imageUrl: String?) {
        GlideApp.with(itemView)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .placeholder(android.R.color.darker_gray)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivImageItem)
    }

    companion object {
        fun create(parent: ViewGroup): ImageViewHolder {
            return ImageViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_user_image_list, parent, false)
            )
        }
    }
}