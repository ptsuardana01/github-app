package com.ps2001.githubapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ps2001.githubapp.data.api.ApiConfig
import com.ps2001.githubapp.data.api.ApiService
import com.ps2001.githubapp.data.local.entity.Favorite
import com.ps2001.githubapp.data.local.room.FavoriteDao
import com.ps2001.githubapp.responses.GithubResponse
import com.ps2001.githubapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavRepository private constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao,
    private val appExecutor: AppExecutors,
    ) {

    private val result = MediatorLiveData<Result<List<Favorite>>>()
    private val isFav = MediatorLiveData<Boolean>()

    fun getAllFavUser() : LiveData<Result<List<Favorite>>> {
        result.value = Result.Loading
        val localData = favoriteDao.getAllFav()
        result.addSource(localData) { newFav: List<Favorite> ->
            result.value = Result.Success(newFav)
        }
        return result
    }

    fun insertFavUser(username: String, avaUrl: String) {
        result.value = Result.Loading
        val addFav = Favorite(username, avaUrl)
        appExecutor.diskIO.execute{
            favoriteDao.insert(addFav)
        }

    }

    fun isFavUser(username: String): LiveData<Boolean> {
        appExecutor.diskIO.execute{
            isFav.postValue(favoriteDao.isFavUser(username))
        }
        return isFav
    }

    fun deleteFavUser(username: String) {
        appExecutor.diskIO.execute{
            favoriteDao.delete(username)
        }
    }

    companion object {
        @Volatile
        private var instance: FavRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavoriteDao,
            appExecutors: AppExecutors
        ): FavRepository =
            instance ?: synchronized(this) {
                instance ?: FavRepository(apiService, favoriteDao, appExecutors)
            }.also { instance = it }
    }
}