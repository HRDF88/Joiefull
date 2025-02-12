package com.nedrysystems.joiefull.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nedrysystems.joiefull.R

/**
 * Displays the category header for a specific product category.
 *
 * @param category The name of the product category to display.
 */
@Composable
fun CategoryHeader(
    category: String,
    textStyle: TextStyle = TextStyle(fontSize = 20.sp),
) {
    val categoryTextContentDescription = stringResource(R.string.category, category)
    Text(
        text = category,
        style = textStyle,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(vertical = 5.dp)
            .semantics { contentDescription = categoryTextContentDescription }
            .padding(4.dp),


        )
}