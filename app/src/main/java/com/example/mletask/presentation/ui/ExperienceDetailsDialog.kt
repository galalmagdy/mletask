package com.example.mletask.presentation.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mletask.presentation.viewmodel.ExperienceDetailsViewModel

@Composable
fun ExperienceDetailsDialog(
    id: String,
    onDismiss: () -> Unit,
    viewModel: ExperienceDetailsViewModel = hiltViewModel()
) {
    val experience by viewModel.experienceDetails.collectAsState()
    var isLiked by remember { mutableStateOf(false) }

    // Load experience details when the dialog opens
    LaunchedEffect(id) {
        viewModel.loadExperienceDetails(id)
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false) // Disable platform default width
    ) {
        Surface(
            shape = RoundedCornerShape(0.dp), // No rounded corners to make it full width
            color = Color.White,
            modifier = Modifier.fillMaxSize() // Ensure full-screen width & height
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                experience?.let { exp ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Image Section with Close Button
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(exp.searchedExperience.imageUrl),
                                    contentDescription = exp.searchedExperience.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Close Button (Top-Right)
                                IconButton(
                                    onClick = { onDismiss() },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp)
                                        .background(Color.White.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = Color.Black
                                    )
                                }

                                // Explore Now Button (Centered on Image)
                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(8.dp)
                                ) {
                                    Text("EXPLORE NOW", fontWeight = FontWeight.Bold, color = Color.Red)
                                }

                                // Views Section (Bottom)
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Face, // Fixed: Use Visibility Icon
                                        contentDescription = "Views",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "${exp.searchedExperience.views} views", color = Color.White)
                                }
                            }
                        }

                        // Experience Title & Likes
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = exp.searchedExperience.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            if (!isLiked) {
                                                viewModel.likeExperience(exp.searchedExperience.id)
                                                isLiked = true
                                            }

                                                  },
                                        enabled = !isLiked
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Likes",
                                            tint = if (isLiked) Color.Red else Color.Gray
                                        )
                                    }
                                    Text(
                                        text = exp.searchedExperience.likes.toString(),
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }

                        // Location
                        item {
                            Text(
                                text = exp.searchedExperience.location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        // Divider Line
                        item {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                        }

                        // Description Section
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = exp.searchedExperience.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Justify
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}