package com.example.mletask.presentation.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mletask.presentation.viewmodel.ExperienceDetailsViewModel

@Composable
fun ExperienceDetailsScreen(
    id: String,
    viewModel: ExperienceDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val experience by viewModel.experienceDetails.collectAsState()
    viewModel.loadExperienceDetails(id)

    experience?.let { exp ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                modifier = Modifier.fillMaxSize() // Makes the card take the whole screen
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Image Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(exp.searchedExperience.imageUrl),
                            contentDescription = exp.searchedExperience.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Back Button (Top-Left)
                        IconButton(
                            onClick = { onBack() },
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.TopStart)
                                .background(Color.Transparent, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }

                        // Explore Button (Center of Image)
                        Button(
                            onClick = {  },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(100.dp)
                                .background(Color.Transparent, RoundedCornerShape(8.dp))

                        ) {
                            Text("Explore", color = Color.Magenta)
                        }

                        // Likes & Favorite Icon (Bottom-Right)
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.likeExperience(exp.searchedExperience.id) },
                            ){
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Likes",
                                        tint = Color.Red
                                    )
                                }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = exp.searchedExperience.likes.toString(),
                                color = Color.White
                            )
                        }

                        // Title & Location (Bottom-Left)
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(6.dp)
                        ) {
                            Text(
                                text = exp.searchedExperience.title,
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                //text = exp.searchedExperience.location,
                                text = "Egypt",
                                style = MaterialTheme.typography.body2,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Divider Line
                    Divider(color = Color.Gray, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Section
                    Text(
                        text = exp.searchedExperience.description,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}