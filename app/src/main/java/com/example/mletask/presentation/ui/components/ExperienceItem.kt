package com.example.mletask.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.example.mletask.data.model.Experience

@Composable
fun ExperienceItem(experience: Experience, onClick: () -> Unit, onLike: () -> Unit) {
    Card(modifier = Modifier.clickable { onClick() }) {
        Column {
            Image(painter = rememberAsyncImagePainter(experience.imageUrl), contentDescription = null)
            Text(text = experience.title)
            Row {
                Text(text = "${experience.likes} Likes")
                IconButton(onClick = onLike) {
                    Icon(Icons.Default.Favorite, contentDescription = "Like")
                }
            }
        }
    }
}