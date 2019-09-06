package com.tatvasoft.engineeringaiapp.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.tatvasoft.engineeringaiapp.network.ApiInterface
import com.tatvasoft.engineeringaiapp.util.NetworkState
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

class UserListDataSource(
        private val apiCall: ApiInterface,
        private val retryExecutor: Executor) : PageKeyedDataSource<Int, ApiInterface.UserListModel.Data.Users>() {

    companion object {
        const val OFFSET = 10
        const val LIMIT = 10
    }


    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ApiInterface.UserListModel.Data.Users>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ApiInterface.UserListModel.Data.Users>) {

        networkState.postValue(NetworkState.LOADING)

        apiCall.getSearchByDate(
                params.key, LIMIT).enqueue(
                object : retrofit2.Callback<ApiInterface.UserListModel> {
                    override fun onFailure(call: Call<ApiInterface.UserListModel>, t: Throwable) {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                    }

                    override fun onResponse(
                            call: Call<ApiInterface.UserListModel>,
                            response: Response<ApiInterface.UserListModel>) {
                        if (response.isSuccessful) {
                            val data = response.body()?.data
                            val items = data?.users ?: emptyList()
                            retry = null
                            callback.onResult(items, params.key + OFFSET)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            retry = {
                                loadAfter(params, callback)
                            }
                            networkState.postValue(
                                    NetworkState.error("error code: ${response.code()}")
                            )
                        }
                    }
                }
        )
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ApiInterface.UserListModel.Data.Users>) {

        val request = apiCall.getSearchByDate(0, LIMIT)
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            val data = response.body()?.data
            val items = data?.users ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items, OFFSET, 0 + OFFSET)
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }
}
