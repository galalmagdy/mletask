package com.example.mletask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mletask.data.model.Experience
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperienceDao {
    @Query("SELECT * FROM experiences WHERE isRecommended = 1")
    fun getRecommended(): Flow<List<Experience>>

    @Query("SELECT * FROM experiences")
    fun getRecent(): Flow<List<Experience>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(experiences: List<Experience>)
}