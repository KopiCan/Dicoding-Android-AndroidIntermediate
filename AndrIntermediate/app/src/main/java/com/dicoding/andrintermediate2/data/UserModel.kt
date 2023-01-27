package com.dicoding.andrintermediate2.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var token: String? = ""
) : Parcelable
