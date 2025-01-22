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

@Singleton
object GlideUtils : ImageLoader {

    override fun loadImage(imageView: ImageView, url: String, placeholder: Int?, error: Int?) {
        val glideRequest = Glide.with(imageView.context).load(url)

        if (placeholder != null) glideRequest.placeholder(placeholder)
        if (error != null) glideRequest.error(error)

        glideRequest.into(imageView)
    }

    @Composable
    override fun loadImagePainter(url: String, placeholder: Int?, error: Int?): Painter {
        val context = LocalContext.current
        val painter = remember(url) {
            // Utiliser une coroutine pour charger l'image de manière asynchrone avec Dispatchers.IO
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
