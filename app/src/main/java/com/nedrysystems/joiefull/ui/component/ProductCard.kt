package com.nedrysystems.joiefull.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.ui.theme.joiefullBackground
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader

/**
 * Displays a product card containing the product's image, name, price, and like counter.
 * It also allows the user to like or dislike the product by interacting with the like button.
 *
 * @param product The [ProductUiModel] representing the product to be displayed.
 * @param imageLoader The [ImageLoader] used to load the product's image.
 * @param modifier The [Modifier] to apply additional layout customizations.
 */
@Composable
fun ProductCard(
    product: ProductUiModel,
    imageLoader: ImageLoader,
    likeCount: Int,
    onLikeClick: () -> Unit,
    onProductClick: () -> Unit,
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(fontSize = 12.sp),
    starModifier : Modifier = Modifier,
    cardElevation: Dp = 4.dp,
    cropImage : ContentScale = ContentScale.Crop
) {
    // Product card layout
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .wrapContentHeight(),
        elevation = cardElevation,


        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Image and like button section
            val painter = imageLoader.loadImagePainter(
                url = product.picture.url,
                placeholder = R.drawable.error,
                error = R.drawable.error
            )

            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = boxModifier
                    .width(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onProductClick)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Image du produit ${product.name}, sa description est ${product.picture.description}",
                    contentScale = cropImage,
                    modifier = imageModifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .align(Alignment.Center)
                )


                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    LikeCounter(
                        likeCount = likeCount,
                        isLiked = product.favorite, // Use current 'isLiked' state
                        onLikeClick = onLikeClick

                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display product name and rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    textAlign = TextAlign.Start,
                    style = textStyle,
                    modifier = Modifier.semantics{"le produit est ${product.name}"}

                    )

                if (product.rate != null) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = joiefullBackground,
                        modifier = starModifier.size(14.dp)
                            .align(Alignment.Bottom)
                    )
                } else {

                    Spacer(modifier = Modifier.size(0.dp))
                }

                Text(
                    text = if (product.rate != null) "${product.rate} ️" else "",
                    textAlign = TextAlign.End,
                    style = textStyle,
                    modifier = Modifier.semantics { contentDescription = "le produit est noté ${product.rate} étoiles sur 5" },
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display price and original price (striked through if available)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,

            ) {
                Text(
                    text = "${product.price} €",
                    style = textStyle,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.semantics{contentDescription = "le prix de ${product.name} est de ${product.price} euros"}

                    )
                Spacer(modifier = Modifier.weight(1f))

                product.originalPrice?.let { originalPrice ->
                    if (originalPrice > product.price) {
                        Text(
                            text = "$originalPrice €",
                            style = textStyle,
                            color = Color.Black,
                            textDecoration = TextDecoration.LineThrough, // Crossed out the original price
                            modifier = Modifier.semantics{contentDescription = "le prix originel de ${product.name} était de ${product.originalPrice} euros"}
                                .align(Alignment.Bottom)


                        )
                    }
                }
            }
        }
    }
}