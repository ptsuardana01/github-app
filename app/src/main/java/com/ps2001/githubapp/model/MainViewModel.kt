package com.ps2001.githubapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps2001.githubapp.api.ApiConfig
import com.ps2001.githubapp.response.GithubResponse
import com.ps2001.githubapp.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _vmListUser = MutableLiveData<List<ItemsItem>>()
    val vmListUser : LiveData<List<ItemsItem>> = _vmListUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        getListUser(QUERY_USER)
    }

    private fun getListUser(query : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserGithub(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _vmListUser.value = response.body()?.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setSearchQuery(query: String) {
        getListUser(query)
    }


    companion object {
        private const val TAG = "Main_VM"
        private const val QUERY_USER = "arif"
    }

}