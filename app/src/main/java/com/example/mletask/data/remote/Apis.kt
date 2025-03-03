package com.example.mletask.data.remote

import com.example.mletask.data.model.Experience
import com.example.mletask.data.model.ExperienceByIdResponse
import com.example.mletask.data.model.ExperienceResponse
import com.example.mletask.data.model.LikeResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface  Apis {
    @GET("api/v2/experiences?filter[recommended]=true")
    suspend fun getRecommendedExperiences(): ExperienceResponse

    @GET("api/v2/experiences")
    suspend fun getRecentExperiences(): ExperienceResponse

    @GET("api/v2/experiences?filter[title]={search_text}")
    suspend fun searchExperiences(@Query("filter[title]") searchText: String): ExperienceResponse

    @GET("api/v2/experiences/{id}")
    suspend fun getExperienceDetails(@Path("id") id: String): ExperienceByIdResponse

    @POST("api/v2/experiences/{id}/like")
    suspend fun likeExperience(@Path("id") id: String): LikeResponse
}