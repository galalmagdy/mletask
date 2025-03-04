package com.example.mletask.data.repository

import com.example.mletask.data.local.ExperienceDao
import com.example.mletask.data.model.Experience
import com.example.mletask.data.model.ExperienceByIdResponse
import com.example.mletask.data.model.LikeResponse
import com.example.mletask.data.remote.Apis
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExperienceRepository @Inject constructor(
    private val api: Apis,
    private val dao: ExperienceDao
) {
    fun getRecommended(): Flow<List<Experience>> = dao.getRecommended()

    //suspend fun getRecommendedLive(): List<Experience> = api.getRecommendedExperiences().experiences

    fun getRecent(): Flow<List<Experience>> = dao.getRecent()

    suspend fun refreshExperiences() {
        val experiences = api.getRecentExperiences()
        dao.insertAll(experiences.experiences)
    }

    suspend fun likeExperience(id: String): LikeResponse {
        return api.likeExperience(id)
    }
    suspend fun selectedExperience(id: String): ExperienceByIdResponse {
        return api.getExperienceDetails(id)
    }
    suspend fun searchExperiences(query: String): List<Experience> {
        return api.searchExperiences(query).experiences
    }
}