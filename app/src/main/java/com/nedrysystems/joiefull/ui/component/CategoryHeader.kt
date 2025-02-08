package com.nedrysystems.joiefull.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Displays the category header for a specific product category.
 *
 * @param category The name of the product category to display.
 */
@Composable
fun CategoryHeader(category: String,
                   textStyle: TextStyle = TextStyle(fontSize = 20.sp)
) {
    Text(
        text = category,
        style = textStyle,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 5.dp),
    )
}