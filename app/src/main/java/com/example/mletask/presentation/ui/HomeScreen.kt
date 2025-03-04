package com.example.mletask.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mletask.presentation.ui.components.ExperienceItem
import com.example.mletask.presentation.viewmodel.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val experiences by viewModel.experiences.collectAsState()
    val recommendedExperiences by viewModel.recommendedExperiences.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    var mainSearchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    var showExperienceDialog by remember { mutableStateOf(false) }
    var selectedExperienceId by remember { mutableStateOf("") }

    // Create a PagerState to control the HorizontalPager
    val pagerState = rememberPagerState()

    // LaunchedEffect to handle auto-scrolling
    LaunchedEffect(pagerState) {
        while (true) {
            // Delay between scrolls (e.g., 2 seconds)
            delay(4000)

            // Calculate the next page
            val nextPage = if (pagerState.currentPage == recommendedExperiences.size - 1) {
                // If at the end, start scrolling backward
                pagerState.currentPage - 1
            } else {
                // Otherwise, scroll forward
                pagerState.currentPage + 1
            }

            // Animate to the next page
            pagerState.animateScrollToPage(nextPage.coerceIn(0, recommendedExperiences.size - 1))
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        item {
            OutlinedTextField(
                value = mainSearchQuery,
                onValueChange = { mainSearchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search experiences...") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                trailingIcon = {
                    IconButton(onClick = {
                        showDialog = true
                        if (mainSearchQuery.isNotBlank()) {
                            viewModel.searchExperiences(mainSearchQuery)
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Filter Icon")
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.Transparent)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Welcome!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Now you an explore any experience in 360 degrees and get all the details about it all in one place",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold)
        }

        // Recommended Experiences
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Recommended Experiences", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            HorizontalPager(
                count = recommendedExperiences.size,
                state = pagerState, // Pass the PagerState here
                modifier = Modifier.fillMaxWidth().height(250.dp)
            ) { page ->
                ExperienceItem(
                    experience = recommendedExperiences[page],
                    onClick = {
                        selectedExperienceId = recommendedExperiences[page].id
                        showExperienceDialog = true
                    },
                    onLike = { viewModel.likeExperience(recommendedExperiences[page].id) }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Most Recent", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        // Most Recent Experiences
        items(experiences) { experience ->
            ExperienceItem(
                experience = experience,
                onClick = {
                    selectedExperienceId = experience.id
                    showExperienceDialog = true
                },
                onLike = { viewModel.likeExperience(experience.id) }
            )
        }
    }

    // 🔹 Experience Details Dialog (Opens when an experience is clicked)
    if (showExperienceDialog) {
        mainSearchQuery = ""
        ExperienceDetailsDialog(
            id = selectedExperienceId,
            onDismiss = { showExperienceDialog = false }
        )
    }

    // 🔹 Full-Screen Search Dialog
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                var searchQuery by remember { mutableStateOf(mainSearchQuery) }

                LaunchedEffect(Unit) {
                    if (searchQuery.isNotBlank()) {
                        viewModel.searchExperiences(searchQuery)
                    }
                }

                // Close Button
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = {
                            showDialog = false
                            mainSearchQuery = ""
                            viewModel.clearSearchResults()
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchExperiences(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search experiences...") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.Transparent)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(searchResults) { experience ->
                        ExperienceItem(
                            experience = experience,
                            onClick = {
                                showDialog = false
                                selectedExperienceId = experience.id
                                showExperienceDialog = true
                            },
                            onLike = { viewModel.likeExperience(experience.id) }
                        )
                    }
                }
            }
        }
    }
}