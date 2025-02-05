package com.nedrysystems.joiefull.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nedrysystems.joiefull.ui.component.ProductCard
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import javax.inject.Inject

class DetailLayout @Inject constructor(private val imageLoader: ImageLoader) {
    @Composable
    fun render() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ProductCard(product = product,
                imageLoader = imageLoader,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}