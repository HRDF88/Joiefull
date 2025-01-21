package com.nedrysystems.joiefull.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Utility object for loading images using the Glide library.
 */
object GlideUtils {
    /**
     * Loads an image into an ImageView from a given URL using Glide.
     *
     * @param imageView The ImageView in which the image will be displayed.
     * @param url The URL of the image to be loaded.
     * @param placeholder Optional. A drawable resource ID to be used as a placeholder
     *                    while the image is being loaded.
     * @param error Optional. A drawable resource ID to be displayed if an error occurs
     *              while loading the image.
     *
     * Example usage:
     * ```
     * GlideUtils.loadImage(imageView, "https://example.com/image.jpg", R.drawable.placeholder, R.drawable.error)
     * ```
     */
    fun loadImage(imageView: ImageView, url: String, placeholder: Int? = null, error: Int? = null) {
        val glideRequest = Glide.with(imageView.context)
            .load(url)

        //Apply placeholders if provided
        if (placeholder != null) {
            glideRequest.placeholder(placeholder)
        }
        if (error != null) {
            glideRequest.error(error)
        }

        glideRequest.into(imageView)
    }
}