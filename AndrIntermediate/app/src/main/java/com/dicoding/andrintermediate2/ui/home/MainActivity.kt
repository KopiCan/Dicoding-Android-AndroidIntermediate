package com.dicoding.andrintermediate2.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.dicoding.andrintermediate2.R
import com.dicoding.andrintermediate2.data.ProfilePreferences
import com.dicoding.andrintermediate2.data.UserModel
import com.dicoding.andrintermediate2.databinding.ActivityMainBinding
import com.dicoding.andrintermediate2.ui.login.LoginActivity
import com.dicoding.andrintermediate2.ui.maps.MapsActivity
import com.dicoding.andrintermediate2.ui.newstory.NewStoryActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userModel: UserModel
    private lateinit var profilePreferences: ProfilePreferences

    private var token : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val supportFragmentManager = supportFragmentManager
        val homeFragment = HomeFramgent()
        val fragment = supportFragmentManager.findFragmentByTag(HomeFramgent::class.java.simpleName)

        if (fragment !is HomeFramgent) {
            Log.d("MyFlexibleFragment", "Fragment Name :" + HomeFramgent::class.java.simpleName)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.home, homeFragment, HomeFramgent::class.java.simpleName)
                .commit()
        }

        profilePreferences = ProfilePreferences(this)
        userModel = profilePreferences.getUser()
        token = userModel.token.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            R.id.map -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(EXTRA_KEYS, token)
                startActivity(intent)
                true
            }
            R.id.createStory -> {
                val intent = Intent(this, NewStoryActivity::class.java)
                intent.putExtra(EXTRA_KEYS, token)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun logout() {
        userModel.token = ""
        profilePreferences.setUser(userModel)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    companion object {
        const val EXTRA_KEYS = "extra_keys"
    }
}