package com.deitel.winfoxtesttask1.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deitel.winfoxtesttask1.data.MyRepository
import com.deitel.winfoxtesttask1.model.UserInfo
import kotlinx.coroutines.launch
import retrofit2.Response

class MyViewModel(private val myRepository: MyRepository): ViewModel() {
    var mMyResponse : MutableLiveData<Response<UserInfo>> = MutableLiveData()

    fun checkUser(userInfo: UserInfo) {
        viewModelScope.launch {
            val response = myRepository.checkUser(userInfo)
            mMyResponse.value = response
        }
    }
}