package com.tatvasoft.engineeringaiapp.adapter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.tatvasoft.engineeringaiapp.network.ApiInterface
import com.tatvasoft.engineeringaiapp.paging.UserRepository
import java.util.concurrent.Executors

class UserViewModel : ViewModel() {
    private val repository = UserRepository(ApiInterface.create(), Executors.newFixedThreadPool(5))
    private val userName = MutableLiveData<String>()
    private val repoResult = map(userName) {
        repository.postUserList(it, 10)
    }
    val posts = switchMap(repoResult) { it.pagedList }!!
    val networkState = switchMap(repoResult) { it.networkState }!!
    val refreshState = switchMap(repoResult) { it.refreshState }!!

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun showUser(userName: String): Boolean {
        if (this.userName.value == userName) {
            return false
        }
        this.userName.value = userName
        return true
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }
}
