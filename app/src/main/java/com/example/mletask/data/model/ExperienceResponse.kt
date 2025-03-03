package com.example.mletask.data.model

import com.google.gson.annotations.SerializedName

data class ExperienceResponse(
    @SerializedName("data") val experiences: List<Experience>
)