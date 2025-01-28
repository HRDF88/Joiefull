package com.nedrysystems.joiefull.utils.image.library

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Singleton

/**
 * A singleton object that provides image loading functionality using the Glide library.
 * Implements the [ImageLoader] interface to load images into views and compose UI elements in a Jetpack Compose environment.
 * It allows loading images asynchronously into both traditional [ImageView] and Jetpack Compose [Image] elements.
 *
 * This object offers two methods:
 * - `loadImage`: Loads an image into an [ImageView] with optional placeholders and error images.
 * - `loadImagePainter`: Loads an image asynchronously and returns a [Painter] to be used in Jetpack Compose UI components.
 */
@Singleton
object GlideUtils : ImageLoader {

    /**
     * Loads an image into the provided [ImageView] using Glide.
     * Optionally supports placeholders and error images in case of loading failure.
     *
     * @param imageView The [ImageView] where the image will be loaded.
     * @param url The URL of the image to be loaded.
     * @param placeholder The resource ID of the placeholder image to be displayed while the image is loading (optional).
     * @param error The resource ID of the error image to be displayed if loading the image fails (optional).
     */
    override fun loadImage(imageView: ImageView, url: String, placeholder: Int?, error: Int?) {
        val glideRequest = Glide.with(imageView.context).load(url)

        if (placeholder != null) glideRequest.placeholder(placeholder)
        if (error != null) glideRequest.error(error)

        glideRequest.into(imageView)
    }

    /**
     * Loads an image asynchronously and returns a [Painter] to be used in Jetpack Compose.
     * This function makes use of a coroutine to load the image on a background thread using [Dispatchers.IO].
     * Optionally supports placeholders and error images.
     *
     * @param url The URL of the image to be loaded.
     * @param placeholder The resource ID of the placeholder image to be displayed while the image is loading (optional).
     * @param error The resource ID of the error image to be displayed if loading the image fails (optional).
     * @return A [Painter] that can be used in Jetpack Compose to display the loaded image.
     */
    @Composable
    override fun loadImagePainter(url: String, placeholder: Int?, error: Int?): Painter {
        val context = LocalContext.current
        val painter = remember(url) {
            //coroutine pour charger l'image de manière asynchrone avec Dispatchers.IO
            val drawable = runBlocking {
                withContext(Dispatchers.IO) {
                    Glide.with(context)
                        .asDrawable()
                        .load(url)
                        .apply {
                            if (placeholder != null) placeholder(placeholder)
                            if (error != null) error(error)
                        }
                        .submit()
                        .get()
                }
            }

            drawable.toPainter()
        }

        return painter
    }


    /**
     * Converts a [Drawable] into a [Painter] that can be used in Jetpack Compose UI.
     *
     * @return A [Painter] representing the [Drawable] that can be used in Compose UI components.
     */
    private fun Drawable.toPainter(): Painter {
        val bitmap = if (this is BitmapDrawable) {
            this.bitmap
        } else {
            val width = intrinsicWidth.takeIf { it > 0 } ?: 1
            val height = intrinsicHeight.takeIf { it > 0 } ?: 1
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            this.setBounds(0, 0, canvas.width, canvas.height)
            this.draw(canvas)
            bitmap
        }
        return BitmapPainter(bitmap.asImageBitmap())
    }
}
