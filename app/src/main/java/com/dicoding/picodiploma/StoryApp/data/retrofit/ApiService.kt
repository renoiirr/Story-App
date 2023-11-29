package com.dicoding.picodiploma.StoryApp.data.retrofit

import com.dicoding.picodiploma.StoryApp.data.api.DetailResponse
import com.dicoding.picodiploma.StoryApp.data.api.Login
import com.dicoding.picodiploma.StoryApp.data.api.LoginResponse
import com.dicoding.picodiploma.StoryApp.data.api.Register
import com.dicoding.picodiploma.StoryApp.data.api.Response
import com.dicoding.picodiploma.StoryApp.data.api.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    suspend fun signup(
        @Body requestBody: Register
    ): Response

    @POST("login")
    suspend fun login(
        @Body requestBody: Login
    ): LoginResponse

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun upStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): StoryResponse

}