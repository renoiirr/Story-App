package com.dicoding.picodiploma.StoryApp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.StoryApp.data.Result
import com.dicoding.picodiploma.StoryApp.data.pref.UserModel
import com.dicoding.picodiploma.StoryApp.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.StoryApp.view.ViewModelFactory
import com.dicoding.picodiploma.StoryApp.view.main.MainActivity
import com.dicoding.picodiploma.StoryApp.view.main.MainViewModel

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.findInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnLogin = binding.loginButton
        val loginEmail = binding.emailEditText
        val loginPassword = binding.passwordEditText

        isAlreadyLogin(this)

        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= 33) {
            val data = intent.getParcelableExtra("extra_email_username", UserModel::class.java)
            if (data != null) {
                loginUser(data.email.toString(), data.password.toString())
            }
        } else {
            val data = intent.getParcelableExtra<UserModel>("extra_email_username")
            if (data != null) {
                loginUser(data.email.toString(), data.password.toString())
            }
        }

        btnLogin.setOnClickListener {
            if (loginPassword.text?.isEmpty() == true) {
                loginPassword.error = "Mohon isi data dengan benar"
            }

            if (loginEmail.text?.isEmpty() == true) {
                loginEmail.error = "Mohon isi data dengan benar"
            }
            if (loginPassword.error == null && loginEmail.error == null) {
                loginUser(loginEmail.text.toString(), loginPassword.text.toString())
            }
        }
        playAnimation()
    }


    private fun isAlreadyLogin(context: Context){
        viewModel.getSession(context).observe(this){token ->
            if (token?.isEmpty()==false){
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun loginUser(email: String, password: String){
        viewModel.loginUser(email, password).observe(this){ output ->
            if (output != null){
                when (output){
                    is Result.Loading ->{
                        loadingProcess()
                    }
                    is Result.Success ->{
                        binding.progressBar.visibility = View.GONE
                        val info = output.data
                        if(info.loginResult?.token != null){
                            viewModel.saveSession(info.loginResult.token, this)
                        }
                        val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(mainActivity)
                        finish()
                    }
                    is Result.Error ->{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, output.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loadingProcess() {
        binding.progressBar.visibility = View.VISIBLE
        binding.emailEditText.isCursorVisible = false
        binding.passwordEditText.isCursorVisible = false
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

}