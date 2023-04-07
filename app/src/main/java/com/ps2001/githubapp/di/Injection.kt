package com.ps2001.githubapp.di

import android.content.Context
import com.ps2001.githubapp.data.FavRepository
import com.ps2001.githubapp.data.api.ApiConfig
import com.ps2001.githubapp.data.local.room.FavoriteRoomDB
import com.ps2001.githubapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteRoomDB.getInstance(context)
        val dao = database.favDao()
        val appExecutors = AppExecutors()
        return FavRepository.getInstance(apiService, dao, appExecutors)
    }
}