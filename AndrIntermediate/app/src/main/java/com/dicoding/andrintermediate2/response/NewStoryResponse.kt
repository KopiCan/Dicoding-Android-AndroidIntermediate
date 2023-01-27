package com.dicoding.andrintermediate2.response

import com.google.gson.annotations.SerializedName

class NewStoryResponse (

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
