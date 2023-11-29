package com.dicoding.picodiploma.StoryApp.view.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.StoryApp.data.Result
import com.dicoding.picodiploma.StoryApp.data.UserRepository
import com.dicoding.picodiploma.StoryApp.data.api.ListStoryItem
import com.dicoding.picodiploma.StoryApp.data.api.Response
import com.dicoding.picodiploma.StoryApp.data.api.StoryResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val liveDataResult = MutableLiveData<Result<Response>>()
    private val token = MutableLiveData<String?>()

    val stories : LiveData<PagingData<ListStoryItem>> =
        repository.getAllStories().cachedIn(viewModelScope)


    fun signupNewUser(
        nama: String,
        password: String,
        email: String
    ): LiveData<Result<Response>> {
        viewModelScope.launch {
            val result = repository.signupUser(nama, password, email)
            liveDataResult.value = result
        }
        return liveDataResult
    }

    fun loginUser(
        email: String, password: String
    ) = repository.loginUser(email, password)


    fun getSession(
        context: Context
    ): LiveData<String?> {
        val DataToken = repository.getSession(context)
        token.value = DataToken
        return token
    }

    fun saveSession(
        token: String, context: Context
    ) = repository.saveSession(token, context)

    fun getStory(
        token: String
    ) = repository.getStory(token)

    fun getDetail(
        token: String, id: String
    ) = repository.getDetail(token, id)

    fun uploadStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ): LiveData<Result<Response>> {
        viewModelScope.launch {
            val output = repository.uploadStory(token, image, desc)
            liveDataResult.value = output
        }
        return liveDataResult
    }

    fun getStoriesWithLocation(
        token: String
    ): LiveData<Result<StoryResponse>> =
        repository.getStoriesWithLocation(token)

}