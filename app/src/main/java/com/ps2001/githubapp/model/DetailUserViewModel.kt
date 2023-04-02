package com.ps2001.githubapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps2001.githubapp.api.ApiConfig
import com.ps2001.githubapp.response.DetailGithubResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    private val _detailUser = MutableLiveData<DetailUserViewModel>()
    val detailUser : LiveData<DetailUserViewModel> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

//    private fun getDetailUser(username : String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().getDetailUserGithub(username)
//        client.enqueue(object : Callback<DetailGithubResponse>{
//            override fun onResponse(
//                call: Call<DetailGithubResponse>,
//                response: Response<DetailGithubResponse>,
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        _detailUser.value =
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<DetailGithubResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }

    companion object {

    }
}