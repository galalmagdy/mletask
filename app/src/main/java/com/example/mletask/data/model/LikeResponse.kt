package com.example.mletask.data.model

import com.google.gson.annotations.SerializedName

data class LikeResponse(
    @SerializedName("data") val likesNumbers: Int
)
