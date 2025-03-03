package com.example.mletask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mletask.data.local.ExperienceDao
import com.example.mletask.data.model.Experience

@Database(entities = [Experience::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao
}