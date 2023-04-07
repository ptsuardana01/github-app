package com.ps2001.githubapp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ps2001.githubapp.ui.adapters.SectionsPagerAdapter
import com.ps2001.githubapp.databinding.ActivityDetailUserBinding
import com.ps2001.githubapp.model.FavoriteViewModel
import com.ps2001.githubapp.model.MainViewModel
import com.ps2001.githubapp.model.ViewModelFactory
import com.ps2001.githubapp.responses.DetailGithubResponse

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val favoriteViewModel by viewModels<FavoriteViewModel>() {
        ViewModelFactory.getInstance(this)
    }

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
            val viewPager: ViewPager2 = binding.includedTabs.viewPager
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = binding.includedTabs.tabs
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
            Glide.with(this@DetailUserActivity).load(details?.avatarUrl).into(imgUserDetail)
        }

        if (details != null) {
            favoriteViewModel.isFavUser(details.login).observe(this) {
                updateFavBtn(it)
            }
        }

        binding.includedFavFloating.floatingFav.setOnClickListener {
            if (details != null) {
                favoriteViewModel.isFavUser(details.login).observe(this) { isFav ->
                    updateFavBtn(isFav)
                    setFavBtn(details, isFav)
                }
            }
        }
    }

    private fun updateFavBtn(isFav: Boolean) {
        if (isFav) {
            binding.includedFavFloating.floatingFav.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.includedFavFloating.floatingFav.context,
                    R.drawable.ic_baseline_favorite_24
                )
            )
        } else {
            binding.includedFavFloating.floatingFav.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.includedFavFloating.floatingFav.context,
                    R.drawable.ic_baseline_favorite_border_24
                )
            )
        }
    }

    fun setFavBtn(details: DetailGithubResponse, isFav: Boolean) {
        if (isFav) {
            favoriteViewModel.deleteFavUser(details.login)
            binding.includedFavFloating.floatingFav.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.includedFavFloating.floatingFav.context,
                    R.drawable.ic_baseline_favorite_border_24
                )
            )
        } else {
            favoriteViewModel.insertFavUser(details.login, details.avatarUrl)
            binding.includedFavFloating.floatingFav.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.includedFavFloating.floatingFav.context,
                    R.drawable.ic_baseline_favorite_24
                )
            )
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }
}