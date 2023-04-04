package com.ps2001.githubapp.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ps2001.githubapp.local.entity.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteRoomDB : RoomDatabase() {
    abstract fun favDao(): FavoriteDao
    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDB? = null
        @JvmStatic
        fun getDatabase(context: Context): FavoriteRoomDB {
            if (INSTANCE == null) {
                synchronized(FavoriteRoomDB::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteRoomDB::class.java, "fav_database")
                        .build()
                }
            }
            return INSTANCE as FavoriteRoomDB
        }
    }
}