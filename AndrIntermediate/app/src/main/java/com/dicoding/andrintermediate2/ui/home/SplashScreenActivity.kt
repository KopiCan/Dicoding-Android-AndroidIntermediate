package com.dicoding.andrintermediate2.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dicoding.andrintermediate2.R
import com.dicoding.andrintermediate2.data.ProfilePreferences
import com.dicoding.andrintermediate2.data.UserModel
import com.dicoding.andrintermediate2.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var userModel: UserModel
    private lateinit var userPreferences: ProfilePreferences

    private val delay = 3500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        userPreferences = ProfilePreferences(this)
        userModel = userPreferences.getUser()

        Handler(Looper.getMainLooper()).postDelayed({
            if (userModel.token == "") {
                val intentLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentLogin)
            } else {
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
            }
            finish()
        }, delay)

        supportActionBar?.hide()
    }
}