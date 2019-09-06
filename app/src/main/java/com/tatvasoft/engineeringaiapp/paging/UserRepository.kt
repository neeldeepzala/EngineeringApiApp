package com.tatvasoft.engineeringaiapp.paging

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.tatvasoft.engineeringaiapp.network.ApiInterface
import com.tatvasoft.engineeringaiapp.util.UserListing
import java.util.concurrent.Executor

class UserRepository(
        private val apiInterface: ApiInterface,
        private val networkExecutor: Executor) {
    @MainThread
    fun postUserList(subReddit: String, pageSize: Int): UserListing<ApiInterface.UserListModel.Data.Users> {
        val sourceFactory = UserDataSourceFactory(
                apiInterface,
                subReddit,
                networkExecutor
        )

        val livePagedList = sourceFactory.toLiveData(
                pageSize = pageSize,
                fetchExecutor = networkExecutor
        )

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return UserListing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState
        )
    }
}