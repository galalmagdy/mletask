package com.example.mletask.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mletask.data.model.Experience
import com.example.mletask.presentation.viewmodel.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoScrollingHorizontalPager(
    experiences: List<Experience>,
    navController: NavController,
    viewModel: HomeViewModel
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll effect with error prevention
    LaunchedEffect(Unit) {
        if (experiences.isNotEmpty()) { // ✅ Check to prevent divide by zero
            while (true) {
                delay(3000L) // Change slide every 3 seconds
                coroutineScope.launch {
                    val nextPage = (pagerState.currentPage + 1) % experiences.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }

    HorizontalPager(
        count = experiences.size,
        state = pagerState,
        modifier = Modifier.fillMaxWidth().height(250.dp)
    ) { page ->
        ExperienceItem(
            experience = experiences[page],
            onClick = { navController.navigate("details/${experiences[page].id}") },
            onLike = { viewModel.likeExperience(experiences[page].id) }
        )
    }
}