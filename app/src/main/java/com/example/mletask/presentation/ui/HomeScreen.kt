package com.example.mletask.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mletask.presentation.ui.components.ExperienceItem
import com.example.mletask.presentation.viewmodel.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val experiences by viewModel.experiences.collectAsState()
    val recommendedExperiences by viewModel.recommendedExperiences.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    var mainSearchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Search Bar
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
                        viewModel.searchExperiences(mainSearchQuery) // 🔹 Trigger search on open
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Filter Icon")
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.Transparent)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Welcome!", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        // Recommended Experiences
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Recommended Experiences", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        HorizontalPager(
            count = recommendedExperiences.size,
            modifier = Modifier.fillMaxWidth().height(250.dp)
        ) { page ->
            ExperienceItem(
                experience = recommendedExperiences[page],
                onClick = { navController.navigate("details/${recommendedExperiences[page].id}") },
                onLike = { viewModel.likeExperience(recommendedExperiences[page].id) }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Most Recent", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(200.dp).weight(1f)
        ) {
            items(experiences) { experience ->
                ExperienceItem(
                    experience = experience,
                    onClick = { navController.navigate("details/${experience.id}") },
                    onLike = { viewModel.likeExperience(experience.id) }
                )
            }
        }
    }

    // Full-Screen Search Dialog
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Full-screen dialog
                .background(Color.White) // Background to match app theme
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                var searchQuery by remember { mutableStateOf(mainSearchQuery) }

                // Auto-trigger search when dialog opens
                LaunchedEffect(Unit) {
                    if (searchQuery.isNotBlank()) {
                        viewModel.searchExperiences(searchQuery)
                    }
                }

                // Close Button (Top-Right)
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = {
                            showDialog = false
                            mainSearchQuery = "" // Clear input field
                            viewModel.clearSearchResults() // Clear search results
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                    }
                }

                // Search Bar inside Full-Screen Dialog
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
                                navController.navigate("details/${experience.id}")
                            },
                            onLike = { viewModel.likeExperience(experience.id) }
                        )
                    }
                }
            }
        }
    }
}