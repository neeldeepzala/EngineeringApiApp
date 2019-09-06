package com.tatvasoft.engineeringaiapp.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.tatvasoft.engineeringaiapp.network.ApiInterface
import java.util.concurrent.Executor

class UserDataSourceFactory(
        private val apiInterface: ApiInterface,
        private val userName: String,
        private val retryExecutor: Executor) : DataSource.Factory<Int, ApiInterface.UserListModel.Data.Users>() {
    val sourceLiveData = MutableLiveData<UserListDataSource>()
    override fun create(): DataSource<Int, ApiInterface.UserListModel.Data.Users> {
        val source = UserListDataSource(
                apiInterface,
                retryExecutor
        )
        sourceLiveData.postValue(source)
        return source
    }
}