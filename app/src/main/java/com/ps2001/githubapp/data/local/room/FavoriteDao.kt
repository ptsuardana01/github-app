package com.ps2001.githubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ps2001.githubapp.data.local.entity.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: Favorite)

    @Query("DELETE FROM favorite WHERE username = :username")
    fun delete(username: String)

    @Query("SELECT EXISTS (SELECT * FROM favorite WHERE username = :username)")
    fun isFavUser(username: String): Boolean

    @Query("SELECT * from favorite ORDER BY username ASC")
    fun getAllFav(): LiveData<List<Favorite>>
}