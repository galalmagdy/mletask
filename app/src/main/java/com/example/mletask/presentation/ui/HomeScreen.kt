package com.example.mletask.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mletask.presentation.ui.components.ExperienceItem
import com.example.mletask.presentation.viewmodel.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val experiences by viewModel.experiences.collectAsState()
    val recommendedExperiences by viewModel.recommendedExperiences.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Welcome!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp)) // Add spacing between texts
        Text(text = "Discover unique experiences across Egypt")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Recommended Experiences ")

        // Carousel View (Horizontal Pager)
        HorizontalPager(
            count = recommendedExperiences.size,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Adjust height as needed
        ) { page ->
            ExperienceItem(
                experience = recommendedExperiences[page],
                onClick = {
                    navController.navigate("details/${recommendedExperiences[page].id}")
                },
                onLike = {
                    viewModel.likeExperience(recommendedExperiences[page].id)
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Most Recent")
        // Experience List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .weight(1f) // Makes the list take remaining space
        ) {
            items(experiences) { experience ->
                ExperienceItem(
                    experience = experience,
                    onClick = {
                        navController.navigate("details/${experience.id}")
                    },
                    onLike = {
                        viewModel.likeExperience(experience.id)
                    }
                )
            }
        }
    }
}