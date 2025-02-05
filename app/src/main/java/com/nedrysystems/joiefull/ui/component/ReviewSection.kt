package com.nedrysystems.joiefull.ui.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

/**
 * ReviewSection is a composable function that displays a user review section, including a profile image,
 * a rating bar for selecting a rating, and a text field for writing a comment.
 *
 * @param profileImage The painter resource representing the user's profile image.
 * @param rating The current rating value (integer from 0 to 5).
 * @param onRatingChanged Callback function triggered when the rating is changed.
 * @param comment The current comment text entered by the user.
 * @param onCommentChanged Callback function triggered when the comment text changes.
 */
@Composable
fun ReviewSection(
    profileImage: Painter,
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    comment: String,
    onCommentChanged: (String) -> Unit
) {
    // Log pour vérifier si rating change
    Log.d("RatingUpdate", "Évaluation actuelle dans ReviewSection: $rating")

    // Log to check if the rating changes
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image
            Image(
                painter = profileImage,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Rating stars
            RatingBar(
                value = rating,
                onValueChange = { newRating ->
                    Log.d("RatingUpdate", "Nouvelle note: $newRating")
                    onRatingChanged(newRating) // Updates the rating in the parent component
                },
                numStars = 5,
                modifier = Modifier.padding(8.dp)
            )
        }


        Spacer(modifier = Modifier.height(8.dp))

        // Comment input field
        OutlinedTextField(
            value = comment,
            onValueChange = onCommentChanged,
            placeholder = { Text("Partagez ici vos impressions sur cette pièce") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
    }
}
