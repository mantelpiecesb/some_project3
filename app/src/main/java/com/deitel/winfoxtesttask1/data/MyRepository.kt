package com.deitel.winfoxtesttask1.data

import com.deitel.winfoxtesttask1.api.BackendAPI
import com.deitel.winfoxtesttask1.model.Dish
import com.deitel.winfoxtesttask1.model.Place
import com.deitel.winfoxtesttask1.model.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import retrofit2.Response
import javax.inject.Inject

class MyRepository @Inject constructor(private val backendAPI: BackendAPI){
    suspend fun checkUser(userInfo: UserInfo): Response<UserInfo> {
        return backendAPI.sendToAPI(userInfo)
    }

    suspend fun getPlaces(): Flow<Place> {
        return backendAPI.getPlaces().asFlow()
    }

    suspend fun getMenu(): Flow<Dish> {
        return backendAPI.getMenu().asFlow()
    }
}