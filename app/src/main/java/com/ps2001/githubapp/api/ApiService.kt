package com.ps2001.githubapp.api

import com.ps2001.githubapp.responses.DetailGithubResponse
import com.ps2001.githubapp.responses.GithubResponse
import com.ps2001.githubapp.responses.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users") //search/users?q=....
    fun getUserGithub(
        @Query("q") query: String,
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUserGithub(
        @Path("username") username: String,
    ): Call<DetailGithubResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String,
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowings(
        @Path("username") username: String,
    ): Call<List<ItemsItem>>
}