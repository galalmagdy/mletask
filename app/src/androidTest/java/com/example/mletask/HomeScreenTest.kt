package com.example.mletask

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mletask.data.model.Experience
import com.example.mletask.presentation.ui.HomeScreen
import com.example.mletask.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var mockViewModel: HomeViewModel

    @Before
    fun setup() {
        mockViewModel = mockk(relaxed = true) // Mock ViewModel

        // Mock data
        val fakeExperiences = listOf(
            Experience(
                id = "1",
                title = "Pyramids Tour",
                imageUrl = "https://example.com/pyramids.jpg",
                description = "A breathtaking tour of the Great Pyramids of Giza.",
                views = "1000",
                location = "Giza, Egypt",
                likes = 50,
                isRecommended = 1
            ),
            Experience(
                id = "2",
                title = "Nile Cruise",
                imageUrl = "https://example.com/nile_cruise.jpg",
                description = "Enjoy a luxury cruise on the Nile River.",
                views = "750",
                location = "Cairo, Egypt",
                likes = 30,
                isRecommended = 1
            )
        )

        val fakeRecommended = listOf(
            Experience(
                id = "3",
                title = "Desert Safari",
                imageUrl = "https://example.com/desert_safari.jpg",
                description = "An adventurous safari in the Egyptian desert.",
                views = "500",
                location = "Western Desert, Egypt",
                likes = 20,
                isRecommended = 1
            )
        )

        every { mockViewModel.experiences } returns MutableStateFlow(fakeExperiences)
        every { mockViewModel.recommendedExperiences } returns MutableStateFlow(fakeRecommended)
        every { mockViewModel.searchResults } returns MutableStateFlow(emptyList())
    }

    @Test
    fun testHomeScreen_DisplaysExperiences() {
        composeTestRule.setContent {
            HomeScreen(navController = rememberNavController(), viewModel = mockViewModel)
        }

        // Verify UI elements
        composeTestRule.onNodeWithText("Welcome!").assertExists()
        composeTestRule.onNodeWithText("Pyramids Tour").assertExists()
        composeTestRule.onNodeWithText("Nile Cruise").assertExists()
        composeTestRule.onNodeWithText("Desert Safari").assertExists()
    }

    @Test
    fun testSearchDialog_OpensOnClick() {
        composeTestRule.setContent {
            HomeScreen(navController = rememberNavController(), viewModel = mockViewModel)
        }

        // Click on search icon
        composeTestRule.onNodeWithContentDescription("Filter Icon").performClick()

        // Check if dialog appears
        composeTestRule.onNodeWithText("Search experiences...").assertExists()
    }

    @Test
    fun testLikingAnExperience_IncreasesCount() {
        every { mockViewModel.likeExperience(any()) } answers {
            val likedId = firstArg<String>()
            val updatedList = mockViewModel.experiences.value.map {
                if (it.id == likedId) it.copy(likes = it.likes + 1) else it
            }
            every { mockViewModel.experiences } returns MutableStateFlow(updatedList)
        }

        composeTestRule.setContent {
            HomeScreen(navController = rememberNavController(), viewModel = mockViewModel)
        }

        // Click on the first experience's like button
        composeTestRule.onNodeWithContentDescription("Like Icon").performClick()

        // Verify like count increased (assert again)
        composeTestRule.onNodeWithText("51").assertExists()
    }

    @Test
    fun testNavigationOnExperienceClick() {
        val navController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            HomeScreen(navController = navController, viewModel = mockViewModel)
        }

        // Click on first experience
        composeTestRule.onNodeWithText("Pyramids Tour").performClick()

        // Verify navigation triggered
        verify { navController.navigate("details/1") }
    }
}