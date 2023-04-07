package com.ps2001.githubapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ps2001.githubapp.data.local.entity.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteRoomDB : RoomDatabase() {
    abstract fun favDao(): FavoriteDao
    companion object {
        @Volatile
        private var instance: FavoriteRoomDB? = null
        fun getInstance(context: Context): FavoriteRoomDB =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteRoomDB::class.java, "Github.db"
                ).build()
            }
    }
}