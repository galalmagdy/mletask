package com.example.mletask.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val experiences by viewModel.experiences.collectAsState()
    val recommendedExperiences by viewModel.recommendedExperiences.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    var mainSearchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showExperienceDialog by remember { mutableStateOf(false) }
    var selectedExperienceId by remember { mutableStateOf("") }

    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(4000)
            val nextPage = if (pagerState.currentPage == recommendedExperiences.size - 1) 0 else pagerState.currentPage + 1
            pagerState.animateScrollToPage(nextPage.coerceIn(0, recommendedExperiences.size - 1))
        }
    }

    val scrollState = rememberLazyListState()
    val isSearchBarVisible = remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Sticky Header for Search Bar
            stickyHeader {
                AnimatedVisibility(
                    visible = isSearchBarVisible.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        TextField(
                            value = mainSearchQuery,
                            onValueChange = { mainSearchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
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
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                containerColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            // Welcome Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White)
                ) {
                    Text(text = "Welcome!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier
                        .height(4.dp)
                        .background(Color.White))
                    Text(
                        text = "Now you can explore any experience in 360 degrees and get all the details about it all in one place",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Recommended Experiences
            item {
                Spacer(modifier = Modifier
                    .height(8.dp)
                    .background(Color.White))
                Text(
                    text = "Recommended Experiences",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                HorizontalPager(
                    count = recommendedExperiences.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
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
            }

            // Most Recent Experiences
            item {
                Spacer(modifier = Modifier
                    .height(8.dp)
                    .background(Color.White))
                Text(
                    text = "Most Recent",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

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

        // Floating Search Bar
        AnimatedVisibility(
            visible = !isSearchBarVisible.value,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.Transparent),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                TextField(
                    value = mainSearchQuery,
                    onValueChange = { mainSearchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
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
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    }

    // 🔹 Experience Details Dialog
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
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
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
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f)) 
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchExperiences(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.4f)),
                        placeholder = { Text("Search experiences...") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent
                        )
                    )
                }


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