package com.example.mletask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mletask.data.model.Experience
import com.example.mletask.data.model.ExperienceByIdResponse
import com.example.mletask.data.repository.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceDetailsViewModel @Inject constructor(
    private val repository: ExperienceRepository
) : ViewModel() {
    private val _experienceDetails = MutableStateFlow<ExperienceByIdResponse?>(null)
    val experienceDetails: StateFlow<ExperienceByIdResponse?> = _experienceDetails

    fun loadExperienceDetails(id: String) {
        viewModelScope.launch {
            _experienceDetails.value = repository.selectedExperience(id)
        }
    }

    fun likeExperience(id: String) {
        viewModelScope.launch {
            repository.likeExperience(id)
            _experienceDetails.update { currentExperience ->
                currentExperience?.copy(
                    searchedExperience = currentExperience.searchedExperience.copy(
                        likes = currentExperience.searchedExperience.likes + 1
                    )
                )
            }
        }
    }
}