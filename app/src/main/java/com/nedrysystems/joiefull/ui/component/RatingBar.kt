package com.nedrysystems.joiefull.ui.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.ui.theme.joiefullBackground

/**
 * RatingBar is a composable function that displays a row of star icons representing a rating system.
 * The user can select a rating by clicking on a star, which updates the rating value.
 *
 * @param value The current rating value, an integer ranging from 0 to numStars.
 * @param onValueChange Callback function triggered when the user selects a different rating.
 * @param numStars The total number of stars in the rating bar (default is 5).
 * @param modifier Modifier for styling and layout customization.
 */
@Composable
fun RatingBar(
    value: Int,
    onValueChange: (Int) -> Unit,
    numStars: Int = 5,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..numStars) {
            // Determines whether the star should be filled or empty
            val starIcon = if (value >= i) R.drawable.starfull else R.drawable.star

            // Animation effect when clicking a star
            var scale by remember { mutableStateOf(1f) }
            val painter = painterResource(id = starIcon)
            val tintColor = if (value >= i) joiefullBackground else Color.Gray

            Icon(
                painter = painter,
                contentDescription = "Rating Star",
                modifier = Modifier
                    .size(32.dp)
                    .scale(scale)
                    .clickable {
                        Log.d("RatingUpdate", "Étoile cliquée : $i")
                        onValueChange(i)
                    },
                tint = tintColor
            )
        }
    }
}
