package com.deitel.winfoxtesttask1.data

import com.deitel.winfoxtesttask1.api.BackendAPI
import com.deitel.winfoxtesttask1.model.UserInfo
import retrofit2.Response
import javax.inject.Inject

class MyRepository @Inject constructor(private val backendAPI: BackendAPI){
    suspend fun checkUser(userInfo: UserInfo): Response<UserInfo> {
        return backendAPI.sendToAPI(userInfo)
    }
}