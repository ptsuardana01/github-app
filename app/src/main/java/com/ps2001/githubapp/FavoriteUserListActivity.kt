package com.ps2001.githubapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ps2001.githubapp.data.Result
import com.ps2001.githubapp.databinding.ActivityFavoriteUserListBinding
import com.ps2001.githubapp.model.FavoriteViewModel
import com.ps2001.githubapp.model.MainViewModel
import com.ps2001.githubapp.model.ViewModelFactory
import com.ps2001.githubapp.responses.ItemsItem
import com.ps2001.githubapp.ui.adapters.MainAdapter

class FavoriteUserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserListBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val favoriteViewModel by viewModels<FavoriteViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User Github"

        showRecyclerView()

        favoriteViewModel.getAllFavUser().observe(this) { users ->
            if (users != null) {
                when (users) {
                    is Result.Loading -> {
                        binding.loadingScreen.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingScreen.progressBar.visibility = View.GONE
                        val favData = users.data
                        val items = arrayListOf<ItemsItem>()
                        favData.map {
                            val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                            items.add(item)
                        }

                        val listFavUser = MainAdapter(items)
                        binding.rvFavUserList.adapter = listFavUser

                        listFavUser.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: ItemsItem) {
                                val detailIntent = Intent(this@FavoriteUserListActivity, DetailUserActivity::class.java)
                                detailIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                                startActivity(detailIntent)
                            }
                        })
                    }
                    is Result.Error -> {
                        binding.loadingScreen.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + users.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavUserList.addItemDecoration(itemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingScreen.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}