package com.example.mletask.di

import android.app.Application
import androidx.room.Room
import com.example.mletask.data.local.AppDatabase
import com.example.mletask.data.local.ExperienceDao
import com.example.mletask.data.remote.Apis
import com.example.mletask.data.repository.ExperienceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://aroundegypt.34ml.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAroundEgyptApi(retrofit: Retrofit): Apis {
        return retrofit.create(Apis::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "experiences_db").build()
    }

    @Provides
    fun provideExperienceDao(database: AppDatabase): ExperienceDao {
        return database.experienceDao()
    }

    @Provides
    @Singleton
    fun provideExperienceRepository(api: Apis, dao: ExperienceDao): ExperienceRepository {
        return ExperienceRepository(api, dao)
    }
}