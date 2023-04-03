package com.ps2001.githubapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps2001.githubapp.api.ApiConfig
import com.ps2001.githubapp.responses.DetailGithubResponse
import com.ps2001.githubapp.responses.GithubResponse
import com.ps2001.githubapp.responses.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _vmListUser = MutableLiveData<List<ItemsItem>>()
    val vmListUser: LiveData<List<ItemsItem>> = _vmListUser

    private val _vmDetailUser = MutableLiveData<DetailGithubResponse>()
    val vmDetailUser: LiveData<DetailGithubResponse> = _vmDetailUser

    private val _vmListFollowers = MutableLiveData<List<ItemsItem>>()
    val vmListFollowers: LiveData<List<ItemsItem>> = _vmListFollowers

    private val _vmListFollowings = MutableLiveData<List<ItemsItem>>()
    val vmListFollowings: LiveData<List<ItemsItem>> = _vmListFollowings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getListUser(QUERY_USER)
    }

    private fun getListUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserGithub(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>,
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

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUserGithub(username)
        client.enqueue(object : Callback<DetailGithubResponse> {
            override fun onResponse(
                call: Call<DetailGithubResponse>,
                response: Response<DetailGithubResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _vmDetailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailGithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getListFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _vmListFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getListFollowings(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowings(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _vmListFollowings.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }


    companion object {
        private const val TAG = "Main_VM"
        private const val QUERY_USER = "arif"
    }

}