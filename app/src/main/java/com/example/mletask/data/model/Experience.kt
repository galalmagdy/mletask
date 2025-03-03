package com.example.mletask.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "experiences")
data class Experience(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("cover_photo") val imageUrl: String,
    @SerializedName("description") val description: String,
    @SerializedName("likes_no") val likes: Int,
    @SerializedName("recommended") val isRecommended: Int
)