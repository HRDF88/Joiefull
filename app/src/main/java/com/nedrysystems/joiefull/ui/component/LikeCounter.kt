package com.nedrysystems.joiefull.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Displays a like counter that allows the user to toggle the like status of a product.
 *
 * @param likeCount The current number of likes for the product.
 * @param isLiked The current like status of the product.
 * @param onLikeClick The callback function to be called when the like button is clicked.
 */
@Composable
fun LikeCounter(likeCount: Int, isLiked: Boolean, onLikeClick: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable {
                onLikeClick()  // Call the onLikeClick callback when clicked
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Like icon that changes based on the 'isLiked' state
        Icon(
            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Like Icon",
            tint = if (isLiked) Color.Red else Color.Black,
            modifier = Modifier.size(24.dp)
        )


        Spacer(modifier = Modifier.width(6.dp))

        // Display the number of likes
        Text(
            text = likeCount.toString(),
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}