package com.ps2001.githubapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ps2001.githubapp.data.FavRepository
import com.ps2001.githubapp.data.Result
import com.ps2001.githubapp.data.local.entity.Favorite

class FavoriteViewModel(private val favRepository: FavRepository) : ViewModel() {
    fun insertFavUser(username: String, avaUrl: String) {
        favRepository.insertFavUser(username, avaUrl)
    }

    fun isFavUser(username: String) : LiveData<Boolean> {
        return favRepository.isFavUser(username)
    }

    fun deleteFavUser(username: String) {
        favRepository.deleteFavUser(username)
    }

    fun getAllFavUser() : LiveData<Result<List<Favorite>>> {
        return favRepository.getAllFavUser()
    }
}