package com.nedrysystems.joiefull.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Displays the category header for a specific product category.
 *
 * @param category The name of the product category to display.
 */
@Composable
fun CategoryHeader(category: String) {
    Text(
        text = category,
        style = MaterialTheme.typography.h1,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp),
    )
}