package com.dicoding.picodiploma.StoryApp.view.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.StoryApp.data.Result
import com.dicoding.picodiploma.StoryApp.data.api.Story
import com.dicoding.picodiploma.StoryApp.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.StoryApp.view.ViewModelFactory
import com.dicoding.picodiploma.StoryApp.view.main.MainViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.findInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= 33) {
            val id = intent.getStringExtra("id")
            if (id != null) settingStory(mainViewModel, id)
        } else {
            val id = intent.getStringExtra("id") as String
            settingStory(mainViewModel, id)
        }
    }

    private fun settingStory(mainViewModel: MainViewModel, id: String) {
        val token = findToken()
        if (token != null) {
            mainViewModel.getDetail("Bearer $token", id)
                .observe(this@DetailActivity) { desc ->
                    if (desc != null) {
                        when (desc) {
                            is Result.Loading -> {

                            }

                            is Result.Success -> {
                                val info = desc.data.story
                                if (info != null) {
                                    setData(info)
                                }
                            }

                            is Result.Error -> {
                            }
                        }
                    }
                }
        }
    }
    private fun findToken(): String? {
        return mainViewModel.getSession(this).value
    }
    private fun setData(data: Story) {
        Glide.with(this)
            .load(data.photoUrl)
            .into(binding.ivStory)
        binding.tvName.text = data.name
        binding.tvDesc.text = data.description
    }

}