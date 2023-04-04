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
            binding.includedTabs.viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(binding.includedTabs.tabs, binding.includedTabs.viewPager) { tab, position ->
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
            binding.apply {
                loadingScreen.progressBar.visibility = View.VISIBLE
                includedTabs.tabs.visibility = View.INVISIBLE
                followersText.visibility = View.INVISIBLE
                followingsText.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                loadingScreen.progressBar.visibility = View.GONE
                includedTabs.tabs.visibility = View.VISIBLE
                followersText.visibility = View.VISIBLE
                followingsText.visibility = View.VISIBLE
            }
        }
    }

    private fun getDetailUser(details: DetailGithubResponse?) {
        binding.apply {
            usernameDetail.text = "@${details?.login.toString()}"
            nameDetail.text = details?.name.toString()
            followersDetail.text = details?.followers.toString()
            followingDetail.text = details?.following.toString()
            Glide.with(this@DetailUserActivity)
                .load(details?.avatarUrl)
                .into(imgUserDetail)
        }
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