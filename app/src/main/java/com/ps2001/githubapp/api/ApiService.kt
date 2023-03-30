package com.ps2001.githubapp.api

import com.ps2001.githubapp.response.GithubResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users") //search/users?q=....
    fun getUserGithub(
        @Query("q") query: String
    ):Call<GithubResponse>
}