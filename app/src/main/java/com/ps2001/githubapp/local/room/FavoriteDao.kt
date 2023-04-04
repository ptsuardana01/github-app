package com.ps2001.githubapp.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ps2001.githubapp.local.entity.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: Favorite)

    @Update
    fun update(fav: Favorite)

    @Delete
    fun delete(fav: Favorite)

    @Query("SELECT * from note ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Favorite>>
}