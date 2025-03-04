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

    private val _searchResults = MutableStateFlow<List<Experience>>(emptyList())
    val searchResults: StateFlow<List<Experience>> = _searchResults

    private val _searchQuery = MutableStateFlow("")

    init {
        loadExperiences()
        loadRecommendedExperiences()
    }

    private fun loadExperiences() {
        viewModelScope.launch {
            repository.refreshExperiences()
            repository.getRecent().collect {
                _experiences.value = it
            }
        }
    }
    private fun loadRecommendedExperiences() {
        viewModelScope.launch {
            repository.getRecommended().collect {
                _RecommendedExperiences.value = it
            }
        }
    }
/*    fun loadRecommendedExperiencesLive() {
        viewModelScope.launch {
            _RecommendedExperiences.value = repository.getRecommendedLive()
        }
    }*/

    fun likeExperience(id: String) {
        viewModelScope.launch {
            repository.likeExperience(id)
            loadExperiences()
        }
    }
    fun searchExperiences(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            val results = repository.searchExperiences(query)
            _searchResults.value = results
        }
    }
    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }
}