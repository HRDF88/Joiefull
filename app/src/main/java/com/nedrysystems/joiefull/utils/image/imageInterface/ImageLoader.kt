package com.nedrysystems.joiefull.utils.image.imageInterface

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

interface ImageLoader {
    /**
     * Loads an image into an ImageView (XML-based UI).
     *
     * @param imageView The ImageView where the image will be displayed.
     * @param url The URL of the image to load.
     * @param placeholder Optional. A drawable resource ID to be displayed as a placeholder
     *                    while the image is loading.
     * @param error Optional. A drawable resource ID to be displayed if an error occurs
     *              while loading the image.
     */
    fun loadImage(imageView: ImageView, url: String, placeholder: Int? = null, error: Int? = null)

    /**
     * Loads an image and returns a Painter for Jetpack Compose.
     *
     * @param url The URL of the image to load.
     * @param placeholder Optional. A drawable resource ID to be displayed as a placeholder
     *                    while the image is loading.
     * @param error Optional. A drawable resource ID to be displayed if an error occurs
     *              while loading the image.
     *
     * @return A [Painter] to be used in Compose [Image] components.
     */
    @Composable
    fun loadImagePainter(url: String, placeholder: Int?, error: Int?): Painter
}
