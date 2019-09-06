package com.tatvasoft.engineeringaiapp.network

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("api/users")
    fun getSearchByDate(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<UserListModel>

    data class UserListModel(
            val data: Data
    ) {

        data class Data(
                val hasMore: Boolean,
                val users: List<Users>
        ){
            data class Users(
                    val name: String?,
                    val image: String?,
                    var items: MutableList<String>
            )
        }

    }

    companion object {
        private const val BASE_URL = "https://sd2-hiring.herokuapp.com/"
        fun create(): ApiInterface = create(HttpUrl.parse(BASE_URL)!!)
        private fun create(httpUrl: HttpUrl): ApiInterface {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("Api----: ", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiInterface::class.java)
        }
    }
}