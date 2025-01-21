package com.nedrysystems.joiefull.utils

import android.widget.ImageView

/**
 * Interface for loading images into an ImageView.
 * This abstraction allows switching image loading libraries without modifying the views.
 */
interface ImageLoader {
    /**
     * Loads an image into an ImageView.
     *
     * @param imageView The ImageView where the image will be displayed.
     * @param url The URL of the image to load.
     * @param placeholder Optional. A drawable resource ID to be displayed as a placeholder
     *                    while the image is loading.
     * @param error Optional. A drawable resource ID to be displayed if an error occurs
     *              while loading the image.
     *
     * Example usage:
     * ```
     * imageLoader.loadImage(imageView, "https://example.com/image.jpg", R.drawable.placeholder, R.drawable.error)
     * ```
     */
    fun loadImage(imageView: ImageView, url: String, placeholder: Int? = null, error: Int? = null)
}