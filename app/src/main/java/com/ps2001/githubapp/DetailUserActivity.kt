package com.ps2001.githubapp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ps2001.githubapp.adapters.SectionsPagerAdapter
import com.ps2001.githubapp.databinding.ActivityDetailUserBinding
import com.ps2001.githubapp.model.MainViewModel
import com.ps2001.githubapp.responses.DetailGithubResponse

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent?.getStringExtra(EXTRA_USERNAME)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "GithubUser"

        if (username != null) {
            mainViewModel.getDetailUser(username)
            val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        mainViewModel.vmDetailUser.observe(this) {
            getDetailUser(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingScreen.progressBar.visibility = View.VISIBLE
            binding.includedTabs.tabs.visibility = View.INVISIBLE
            binding.followersText.visibility = View.INVISIBLE
            binding.followingsText.visibility = View.INVISIBLE
        } else {
            binding.loadingScreen.progressBar.visibility = View.GONE
            binding.includedTabs.tabs.visibility = View.VISIBLE
            binding.followersText.visibility = View.VISIBLE
            binding.followingsText.visibility = View.VISIBLE
        }
    }

    private fun getDetailUser(details: DetailGithubResponse?) {
        binding.usernameDetail.text = "@${details?.login.toString()}"
        binding.nameDetail.text = details?.name.toString()
        binding.followersDetail.text = details?.followers.toString()
        binding.followingDetail.text = details?.following.toString()
        Glide.with(this)
            .load(details?.avatarUrl)
            .into(binding.imgUserDetail)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}