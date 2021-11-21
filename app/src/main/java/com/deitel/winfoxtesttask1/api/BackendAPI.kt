package com.deitel.winfoxtesttask1.api

import com.deitel.winfoxtesttask1.model.Dish
import com.deitel.winfoxtesttask1.model.Place
import com.deitel.winfoxtesttask1.model.UserInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface BackendAPI {

    @POST("checkUser")
    suspend fun sendToAPI(
        @Body userInfo: UserInfo
    ): Response<UserInfo>

    @GET("getPlaces")
    suspend fun getPlaces(): List<Place>

    @GET("getMenu")
    suspend fun getMenu(): List<Dish>

    companion object {
        private const val BASE_URL = "http://94.127.67.113:8099/"

        fun create(): BackendAPI {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BackendAPI::class.java)
        }
    }
}