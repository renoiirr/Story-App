package com.dicoding.picodiploma.StoryApp.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.StoryApp.R
import com.dicoding.picodiploma.StoryApp.data.Result
import com.dicoding.picodiploma.StoryApp.data.api.ListStoryItem
import com.dicoding.picodiploma.StoryApp.databinding.ActivityMainBinding
import com.dicoding.picodiploma.StoryApp.view.Adapter
import com.dicoding.picodiploma.StoryApp.view.ViewModelFactory
import com.dicoding.picodiploma.StoryApp.view.add.AddStory
import com.dicoding.picodiploma.StoryApp.view.detail.DetailActivity
import com.dicoding.picodiploma.StoryApp.view.maps.MapsActivity
import com.dicoding.picodiploma.StoryApp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.findInstance(application)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: Setting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = Setting(this)
        val token: String = intent.getStringExtra("TOKEN").toString()
        setRecycleView(token)

        isAlreadyLogin(this)

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStory::class.java)
            startActivity(intent)
        }

    }

    private fun setRecycleView(token: String) {
        val adapter = Adapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.adapter = adapter
        viewModel.stories.observe(this){
            adapter.submitData(lifecycle, it)
        }
        adapter.setOnClickListener{story ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id",story.id)
            startActivity(intent)
        }

    }

    private fun isAlreadyLogin(context: Context){
        viewModel.getSession(context).observe(this){token ->
            if (token?.isEmpty()==true){
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option, menu)

        val out = menu.findItem(R.id.logout)
        out.setOnMenuItemClickListener(this)

        val maps = menu.findItem(R.id.menumap)
        maps.setOnMenuItemClickListener(this)
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menumap -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout -> {
                viewModel.saveSession("", this)
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> return false
        }
    }
}