package com.ps2001.githubapp.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    var avatarUrl: String? = null,
) : Parcelable
