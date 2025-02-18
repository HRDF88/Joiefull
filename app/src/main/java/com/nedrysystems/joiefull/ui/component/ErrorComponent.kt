package com.nedrysystems.joiefull.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.nedrysystems.joiefull.R

@Composable
fun ErrorComponent (painter: Painter){
    Box(modifier = Modifier.fillMaxWidth()
        .fillMaxHeight())

    Image(
        painter = painter,
        contentDescription = stringResource(R.string.splash_screen),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}