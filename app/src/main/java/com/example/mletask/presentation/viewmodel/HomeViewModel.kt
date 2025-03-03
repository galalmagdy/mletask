package com.example.mletask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mletask.data.model.Experience
import com.example.mletask.data.repository.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ExperienceRepository
) : ViewModel() {

    private val _experiences = MutableStateFlow<List<Experience>>(emptyList())
    val experiences: StateFlow<List<Experience>> = _experiences
    private val _RecommendedExperiences = MutableStateFlow<List<Experience>>(emptyList())
    val recommendedExperiences: StateFlow<List<Experience>> = _RecommendedExperiences

    init {
        loadExperiences()
        loadRecommendedExperiences()
    }

    fun loadExperiences() {
        viewModelScope.launch {
            repository.refreshExperiences()
            repository.getRecent().collect {
                _experiences.value = it
            }
        }
    }
    fun loadRecommendedExperiences() {
        viewModelScope.launch {
            repository.getRecommended().collect {
                _RecommendedExperiences.value = it
            }
        }
    }

    fun likeExperience(id: String) {
        viewModelScope.launch {
            repository.likeExperience(id)
            loadExperiences()
        }
    }
}