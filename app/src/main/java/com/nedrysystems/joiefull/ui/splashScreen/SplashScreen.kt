package com.nedrysystems.joiefull.ui.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.nedrysystems.joiefull.R
import kotlinx.coroutines.delay

/**
 * A composable function that displays a splash screen with an image for a brief period.
 * After the delay, it triggers the `onFinish` callback to navigate to the main screen.
 *
 * @param onFinish A callback function to be called once the splash screen duration is over.
 *                 It is triggered after the specified delay to navigate to the main screen.
 */
@Composable
fun SplashScreen(onFinish: () -> Unit) {
    // Load the splash image from resources (make sure it's placed in the res/drawable folder)
    val painter =
        painterResource(id = R.drawable.init)

    // Launch a side effect to wait for 2 seconds before navigating to the next screen
    LaunchedEffect(Unit) {
        delay(2000) // Wait for 2 seconds
        onFinish() // Call the onFinish callback to navigate to the main layout
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

        // Display the splash image, adjusting it to cover the entire screen
    ) {
        Image(
            painter = painter,
            contentDescription = "Splash Screen Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

