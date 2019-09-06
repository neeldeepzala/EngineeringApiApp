package com.tatvasoft.engineeringaiapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.tatvasoft.engineeringaiapp.R
import com.tatvasoft.engineeringaiapp.network.ApiInterface
import com.tatvasoft.engineeringaiapp.util.GlideApp

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.tvTitle)
    private val ivUser: ImageView = view.findViewById(R.id.ivUser)
    private val rvImageList: RecyclerView = view.findViewById(R.id.rvImageList)

    fun bind(users: ApiInterface.UserListModel.Data.Users?) {

        title.text = "${users?.name}"
        GlideApp.with(itemView)
                .load(users?.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .placeholder(android.R.color.darker_gray)
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(ivUser)

        rvImageList.layoutManager = GridLayoutManager(itemView.context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when {
                        users?.items.orEmpty().count() % 2 == 0 -> 1
                        else -> when (position) {
                            0 -> 2
                            else -> 1
                        }
                    }
                }
            }
        }
        rvImageList.isNestedScrollingEnabled = false
        rvImageList.adapter = ImageAdapter(users?.items)
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            return UserViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
            )
        }
    }
}