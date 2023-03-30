package com.ps2001.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ps2001.githubapp.adapter.MainAdapter
import com.ps2001.githubapp.api.ApiConfig
import com.ps2001.githubapp.data.User
import com.ps2001.githubapp.databinding.ActivityMainBinding
import com.ps2001.githubapp.response.GithubResponse
import com.ps2001.githubapp.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMain.setHasFixedSize(true)
        showRecyclerView()

        getListUser()
    }

    private fun getListUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserGithub(QUERY_USER)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUser(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUser(items: List<ItemsItem>) {
        val listUsername = ArrayList<User>()
        for (data in items) {
            listUsername.add(
                User(data.login, data.avatarUrl)
            )
        }
        val listUser = MainAdapter(listUsername)
        binding.rvMain.adapter = listUser
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingScreen.progressBar.visibility = View.VISIBLE
        } else {
            binding.loadingScreen.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvMain.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvMain.addItemDecoration(itemDecoration)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val QUERY_USER = "tutur"
    }
}