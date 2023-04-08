package com.ps2001.githubapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ps2001.githubapp.data.local.datastore.SettingPreferences
import com.ps2001.githubapp.ui.adapters.MainAdapter
import com.ps2001.githubapp.databinding.ActivityMainBinding
import com.ps2001.githubapp.model.MainViewModel
import com.ps2001.githubapp.model.SettingModeViewModel
import com.ps2001.githubapp.model.SettingViewModelFactory
import com.ps2001.githubapp.responses.ItemsItem
import com.ps2001.githubapp.ui.DetailUserActivity
import com.ps2001.githubapp.ui.FavoriteUserListActivity
import com.ps2001.githubapp.ui.SettingActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMain.setHasFixedSize(true)
        showRecyclerView()

        mainViewModel.vmListUser.observe(this) {
            setUser(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingModeViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_user, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.setSearchQuery(query)
                mainViewModel.vmListUser.observe(this@MainActivity) {
                    setUser(it)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                /* Jika mengimplementasikan ini, harus memiliki limit req API yg Besar
                mainViewModel.setSearchQuery(newText)
                mainViewModel.vmListUser.observe(this@MainActivity) {
                    setUser(it)
                }
                searchView.clearFocus()
                */
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                val intentToFavList = Intent(this@MainActivity, FavoriteUserListActivity::class.java)
                startActivity(intentToFavList)
            }
            R.id.settings -> {
                val intentToSetting = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intentToSetting)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUser(items: List<ItemsItem>) {
        val listUser = MainAdapter(items)
        binding.rvMain.adapter = listUser

        listUser.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val detailIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                detailIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                startActivity(detailIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingScreen.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvMain.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvMain.addItemDecoration(itemDecoration)
    }

    companion object {

    }
}