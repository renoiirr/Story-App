package com.dicoding.picodiploma.StoryApp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.StoryApp.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.StoryApp.view.ViewModelFactory
import com.dicoding.picodiploma.StoryApp.view.main.MainViewModel
import com.dicoding.picodiploma.StoryApp.data.Result
import com.dicoding.picodiploma.StoryApp.data.pref.UserModel
import com.dicoding.picodiploma.StoryApp.view.login.LoginActivity
import com.dicoding.picodiploma.StoryApp.view.welcome.WelcomeActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.findInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val valName = binding.nameEditText.text
        val valEmail = binding.emailEditText.text
        val valPassword = binding.passwordEditText.text

        supportActionBar?.hide()

        binding.signupButton.setOnClickListener {
            val signupPassword = binding.passwordEditText.text

            if (signupPassword?.length!! <8){
                binding.signupButton.error = "Minimal harus 8 karakter"
            }else{
                loadingCorrect()
                viewModel.signupNewUser(
                    valName.toString(),
                    valPassword.toString(),
                    valEmail.toString()
                ).observe(this){ output ->
                    when (output){
                        is Result.Loading -> {}
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val response = output.data
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            sendToLogin(
                                UserModel(
                                    valEmail.toString(),
                                    valPassword.toString()
                                )
                            )
                        }
                        is Result.Error -> {
                            incorrectData()
                            Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, SignupActivity::class.java))
                        }
                    }
                }
            }
        }
        playAnimation()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
    private fun incorrectData(){
        binding.progressBar.visibility = View.GONE
        binding.nameEditText.isCursorVisible = true
        binding.emailEditText.isCursorVisible = true
        binding.passwordEditText.isCursorVisible = true
    }

    private fun loadingCorrect(){
        binding.progressBar.visibility = View.GONE
        binding.nameEditText.isCursorVisible = false
        binding.emailEditText.isCursorVisible = false
        binding.passwordEditText.isCursorVisible = false
    }


    private fun sendToLogin(data: UserModel){
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        intent.putExtra("extra_email_username", data)
        startActivity(intent)
        finish()
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }

}