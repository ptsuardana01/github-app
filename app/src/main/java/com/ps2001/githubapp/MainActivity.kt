package com.ps2001.githubapp

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
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
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.ps2001.githubapp.model.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMain.setHasFixedSize(true)
        showRecyclerView()

//        getListUser()
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        mainViewModel.vmListUser.observe(this) {
            setUser(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_user, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /*
            Gunakan method ini ketika search selesai atau OK
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                val client = ApiConfig.getApiService().getUserGithub(query)
                client.enqueue(object : Callback<GithubResponse> {
                    override fun onResponse(
                        call: Call<GithubResponse>,
                        response: Response<GithubResponse>,
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) setUser(responseBody.items)
                        } else {
                            Log.e("Search", "onFailure: ${response.message()}")
                            Toast.makeText(this@MainActivity, "Data gagal untuk diproses", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                        showLoading(false)
                        Log.e("Search", "onFailure: ${t.message}")
                        Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
                searchView.clearFocus()
                return true
            }

            /*
            Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
             */
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
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

    companion object {}
}