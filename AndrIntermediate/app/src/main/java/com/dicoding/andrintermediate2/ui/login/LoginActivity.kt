package com.dicoding.andrintermediate2.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.andrintermediate2.data.ProfilePreferences
import com.dicoding.andrintermediate2.data.ResultData
import com.dicoding.andrintermediate2.data.UserModel
import com.dicoding.andrintermediate2.databinding.ActivityLoginBinding
import com.dicoding.andrintermediate2.ui.home.MainActivity
import com.dicoding.andrintermediate2.ui.register.RegisterActivity
import com.dicoding.andrintermediate2.ui.utilitys.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var userModel: UserModel = UserModel()
    private lateinit var profilePreferences: ProfilePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val loginViewModel: LoginViewModel by viewModels { viewModelFactory }

        profilePreferences = ProfilePreferences(this)
        binding.accNotyet.setOnClickListener { moveToRegister() }
        binding.btnLogin.setOnClickListener {
            loginViewModel.postLogin(
                binding.loginFieldEmail.text.toString(),
                binding.loginFieldPassword.text.toString()
            ).observe(this) { resultData ->
                if (resultData != null) {
                    when(resultData) {
                        is ResultData.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is ResultData.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Login ${resultData.data.message}", Toast.LENGTH_SHORT).show()
                            val responseData = resultData.data
                            saveToken(responseData.loginResult.token)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra(EXTRA_KEYS, responseData.loginResult.token)
                            startActivity(intent)
                        }
                        is ResultData.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Login ${resultData.error}" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        playAnimation()

        supportActionBar?.hide()
    }

    private fun playAnimation() {
        val loginTitle = ObjectAnimator.ofFloat(binding.loginTitle, View.ALPHA, 1f).setDuration(650)
        val fieldEmail = ObjectAnimator.ofFloat(binding.loginFieldEmail, View.ALPHA, 1f).setDuration(650)
        val fieldPassword = ObjectAnimator.ofFloat(binding.loginFieldPassword, View.ALPHA, 1f).setDuration(650)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(650)
        val btnSignUp = ObjectAnimator.ofFloat(binding.accNotyet, View.ALPHA, 1f).setDuration(650)

        AnimatorSet().apply {
            playSequentially(loginTitle,fieldEmail,fieldPassword , btnLogin, btnSignUp)
            start()
        }
    }

    private fun moveToRegister() {
        intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun saveToken(token: String) {
        userModel.token = token
        profilePreferences.setUser(userModel)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    companion object {
        const val EXTRA_KEYS = "extra_keys"
    }
}