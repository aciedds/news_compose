package com.example.news.data.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status")
    val staus : String,
    @SerializedName("code")
    val code : String,
    @SerializedName("message")
    val message : String,
)