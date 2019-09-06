package com.tatvasoft.engineeringaiapp.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tatvasoft.engineeringaiapp.R
import com.tatvasoft.engineeringaiapp.network.ApiInterface
import com.tatvasoft.engineeringaiapp.util.NetworkState

class UserListAdapter(private val retryCallback: () -> Unit) : PagedListAdapter<ApiInterface.UserListModel.Data.Users, RecyclerView.ViewHolder>(POST_COMPARATOR) {
    private var networkState: NetworkState? = null

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_user_list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user_list -> UserViewHolder.create(parent)
            R.layout.item_network_state -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user_list -> (holder as UserViewHolder).bind(getItem(position))
            R.layout.item_network_state -> (holder as NetworkStateItemViewHolder).bindTo(networkState)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        onBindViewHolder(holder, position)
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<ApiInterface.UserListModel.Data.Users>() {
            override fun areContentsTheSame(oldItem: ApiInterface.UserListModel.Data.Users, newItem: ApiInterface.UserListModel.Data.Users): Boolean = oldItem == newItem

            override fun areItemsTheSame(oldItem: ApiInterface.UserListModel.Data.Users, newItem: ApiInterface.UserListModel.Data.Users): Boolean = oldItem.name == newItem.name
        }
    }
}