package com.dicoding.andrintermediate2.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.andrintermediate2.R
import com.dicoding.andrintermediate2.data.ResultData
import com.dicoding.andrintermediate2.databinding.ActivityMapsBinding
import com.dicoding.andrintermediate2.response.ListStory
import com.dicoding.andrintermediate2.ui.utilitys.ViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val storyWithMapsViewModel: MapsViewModel by viewModels { viewModelFactory }

        val token = intent.getStringExtra("extra_keys")
        val authToken = "Bearer $token"
        if (token != null) {
            storyWithMapsViewModel.getStoryWithMaps(1, authToken).observe(this) { result ->
                if (result != null) {
                    when(result) {
                        is ResultData.Loading -> {
                        }
                        is ResultData.Success -> {
                            showMarker(result.data.listStory)
                        }
                        is ResultData.Error -> {
                            Toast.makeText(this, result.error , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.hide()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        getMyLocation()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.google_map_custom_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun showMarker(listStory: List<ListStory>) {
        for (story in listStory) {
            val latlng = LatLng(story.lat, story.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .snippet(story.description)
                    .title(story.name)
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}