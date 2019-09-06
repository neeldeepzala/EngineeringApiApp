package com.tatvasoft.engineeringaiapp.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tatvasoft.engineeringaiapp.R
import com.tatvasoft.engineeringaiapp.adapter.UserListAdapter
import com.tatvasoft.engineeringaiapp.adapter.UserViewModel
import com.tatvasoft.engineeringaiapp.network.ApiInterface
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        initAdapter()
        userViewModel.showUser("story")
    }

    private fun initAdapter() {
        adapter = UserListAdapter() { userViewModel.retry() }
        rvUserList.adapter = adapter
        rvUserList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvUserList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        userViewModel.posts.observe(this, Observer<PagedList<ApiInterface.UserListModel.Data.Users>> {
            rvUserList.visibility = View.VISIBLE
            pbUserList.visibility = View.GONE
            adapter.submitList(it)
        })
        userViewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }
}