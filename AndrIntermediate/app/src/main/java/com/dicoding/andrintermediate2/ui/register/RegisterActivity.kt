package com.dicoding.andrintermediate2.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.andrintermediate2.data.ResultData
import com.dicoding.andrintermediate2.databinding.ActivityRegisterBinding
import com.dicoding.andrintermediate2.ui.login.LoginActivity
import com.dicoding.andrintermediate2.ui.utilitys.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val registerViewModel: RegisterViewModel by viewModels {
            viewModelFactory
        }


        binding.accAlready.setOnClickListener { moveToLogin() }
        binding.btnRegister.setOnClickListener {
            registerViewModel.postRegister(
                binding.registerFieldName.text.toString(),
                binding.registerFieldEmail.text.toString(),
                binding.registerFieldPassword.text.toString()
            ).observe(this) { result ->
                when(result) {
                    is ResultData.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultData.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Register ${result.data.message}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }
                    is ResultData.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Register ${result.error}" , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        playAnimation()
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        val registerTitle = ObjectAnimator.ofFloat(binding.registerTitle, View.ALPHA, 1f).setDuration(500)
        val fieldName = ObjectAnimator.ofFloat(binding.registerFieldName, View.ALPHA, 1f).setDuration(500)
        val fieldEmail = ObjectAnimator.ofFloat(binding.registerFieldEmail, View.ALPHA, 1f).setDuration(500)
        val fieldPassword = ObjectAnimator.ofFloat(binding.registerFieldPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.accAlready, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(registerTitle, fieldName, fieldEmail, fieldPassword, btnRegister, btnLogin
            )
            start()
        }
    }

    private fun moveToLogin() {
        intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
    }

}