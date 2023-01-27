package com.dicoding.andrintermediate2.ui.newstory

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.andrintermediate2.R
import com.dicoding.andrintermediate2.data.ResultData
import com.dicoding.andrintermediate2.databinding.ActivityNewStoryBinding
import com.dicoding.andrintermediate2.ui.home.MainActivity
import com.dicoding.andrintermediate2.ui.utilitys.ViewModelFactory
import com.dicoding.andrintermediate2.ui.utilitys.createCustomTempFile
import com.dicoding.andrintermediate2.ui.utilitys.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class NewStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewStoryBinding
    private lateinit var photoPath: String
    private lateinit var locationProviderClient: FusedLocationProviderClient

    private var getFile: File? = null

    private lateinit var viewModelFactory: ViewModelFactory
    private val newStoryViewModel: NewStoryViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val token = intent.getStringExtra("extra_keys")

        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.submitStory.setOnClickListener {
            if (token != null) {
                uploadImage(
                    binding.editTextDescription.text.toString(),
                    token
                )
            }
        }

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.add_story)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadImage(desc: String, token: String) {
        if (getFile != null) {
            val uploadTask = locationProviderClient.lastLocation
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
                return
            }

            var lat: Double
            var lon: Double

            uploadTask.addOnSuccessListener { location : Location? ->
                if (location != null) {

                    val file = downgradeImage(getFile as File)
                    val authToken = "Bearer $token"

                    lat = location.latitude
                    lon = location.longitude

                    val description = desc.toRequestBody("text/plain".toMediaType())
                    val requestImg = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImg
                    )

                    newStoryViewModel.uploadImage(imgMultipart, description, lat , lon, authToken).observe(this) { result ->
                        if (result != null) {
                            when(result) {
                                is ResultData.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                                is ResultData.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this@NewStoryActivity, "Result ${result.data.message}", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@NewStoryActivity, MainActivity::class.java)
                                    intent.putExtra(EXTRA_KEYS, token)
                                    startActivity(intent)
                                }
                                is ResultData.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this, "Result ${result.error}" , Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@NewStoryActivity, R.string.image_mandatory, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intentImg = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intentImg.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@NewStoryActivity,
                "com.dicoding.andrintermediate2",
                it
            )
            photoPath = it.absolutePath
            intentImg.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intentImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)

            getFile = myFile
            val imgResult =  BitmapFactory.decodeFile(myFile.path)

            binding.imgAddStory.setImageBitmap(imgResult)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result->
        if (result.resultCode == RESULT_OK) {
            val imgSelected: Uri = result.data?.data as Uri
            val myFile = uriToFile(imgSelected, this@NewStoryActivity)
            getFile = myFile
            binding.imgAddStory.setImageURI(imgSelected)
        }
    }

    private fun downgradeImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var downgradeQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, downgradeQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            downgradeQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, downgradeQuality, FileOutputStream(file))
        return file
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_KEYS = "extra_keys"
    }
}