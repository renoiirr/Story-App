package com.dicoding.picodiploma.StoryApp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.StoryApp.data.api.DetailResponse
import com.dicoding.picodiploma.StoryApp.data.api.ListStoryItem
import com.dicoding.picodiploma.StoryApp.data.api.Login
import com.dicoding.picodiploma.StoryApp.data.api.LoginResponse
import com.dicoding.picodiploma.StoryApp.data.api.Register
import com.dicoding.picodiploma.StoryApp.data.api.Response
import com.dicoding.picodiploma.StoryApp.data.api.StoryResponse

import com.dicoding.picodiploma.StoryApp.data.retrofit.ApiService
import com.dicoding.picodiploma.StoryApp.view.main.Setting
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val pref: Setting
) {
    suspend fun signupUser(
        nama: String,
        password: String,
        email: String
    ): Result<Response> {
        return try {
            val response = apiService.signup(Register(nama, email, password))
            Result.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()

            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            Result.Error(errorMessage)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(Login(email, password))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun saveSession(token: String, context: Context) {
        val settingPreferences = Setting(context)
        return settingPreferences.putUser(token)
    }

    fun getSession(context: Context): String? {
        val settingPreferences = Setting(context)
        return settingPreferences.gainUser()
    }

    fun getStory(token: String): LiveData<Result<StoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStory("Bearer $token")
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getDetail(token: String, id: String): LiveData<Result<DetailResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val respon = apiService.getDetail(token, id)
                emit(Result.Success(respon))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    suspend fun uploadStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ): Result<Response> {
        return try {
            val response = apiService.upStory(token, image, desc)
            Result.Success(response)
        }  catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    fun getStoriesWithLocation(token: String): LiveData<Result<StoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val respon = apiService.getStoriesWithLocation(token, 1)
                emit(Result.Success(respon))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }



    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        val token = pref.gainUser()
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token.toString())
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: Setting
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref)
            }.also { instance = it }
    }
}