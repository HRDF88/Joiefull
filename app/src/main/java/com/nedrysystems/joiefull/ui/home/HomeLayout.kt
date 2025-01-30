package com.nedrysystems.joiefull.ui.home


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import javax.inject.Inject

/**
 * HomeLayout is a composable UI component responsible for rendering the main screen layout in the application.
 * It displays a list of products grouped by category. Each product is displayed using a [ProductCard] that shows
 * product details like the name, price, and rating, and also allows users to like or dislike the product.
 *
 * @param imageLoader The [ImageLoader] used to load images for the product cards.
 */
class HomeLayout @Inject constructor(private val imageLoader: ImageLoader) {

    /**
     * Renders the main screen layout for the home page, including the list of products grouped by category.
     * It observes the UI state from the [HomeViewModel] and updates the UI accordingly.
     *
     * @param viewModel The [HomeViewModel] instance that provides the data and manages the UI state.
     */
    @Composable
    fun Render(viewModel: HomeViewModel) {
        val uiState by viewModel.uiState.collectAsState()

        // Get the current context for Toast notifications
        val context = LocalContext.current

        // Show a Toast notification if there is an error in the UI state
        LaunchedEffect(uiState.error) {
            if (uiState.error.isNotEmpty()) {
                Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
            }
        }
        // Main layout displaying products
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                else -> {
                    // Group products by category
                    val productsGroupedByCategory = uiState.products.groupBy { it.category }

                    // Display the products by category
                    LazyColumn {
                        productsGroupedByCategory.forEach { (category, products) ->
                            // Display the category header
                            item {
                                CategoryHeader(category = category)
                            }

                            // Display the products in a horizontal scrollable row
                            item {
                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(products) { product ->
                                        Log.d(
                                            "HomeLayout",
                                            "Displaying Product: Name: ${product.name}, Price: ${product.price}, Category: ${product.category}, Original Price: ${product.originalPrice}, Rate: ${product.rate}, Favorite: ${product.favorite}"
                                        )
                                        ProductCard(
                                            product = product,
                                            modifier = Modifier.padding(end = 8.dp),
                                            imageLoader = imageLoader
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Displays the category header for a specific product category.
     *
     * @param category The name of the product category to display.
     */
    @Composable
    fun CategoryHeader(category: String) {
        Text(
            text = category,
            style = MaterialTheme.typography.h1,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }

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
        modifier: Modifier = Modifier,
    ) {
        val homeViewModel: HomeViewModel = viewModel()

        // Product card layout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .wrapContentHeight(),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Image and like button section
                val painter = imageLoader.loadImagePainter(
                    url = product.picture.url,
                    placeholder = R.drawable.error,
                    error = R.drawable.error
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .align(Alignment.Center)
                    )

                    var likeCount by remember { mutableStateOf(product.likes) }
                    var isLiked by remember { mutableStateOf(product.favorite) }
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        LikeCounter(
                            likeCount = likeCount,
                            isLiked = product.favorite, // Use current 'isLiked' state
                            onLikeClick = {
                                // Toggle the favorite status and update the like count
                                val updatedFavoriteStatus = !product.favorite
                                likeCount += if (updatedFavoriteStatus) 1 else -1

                                // Log before update
                                Log.d(
                                    "LikeCounter",
                                    "Produit avant mise à jour: ${product.id} - ${product.name} - Favorite: ${product.favorite}"
                                )

                                // Create a new product with updated favorite status
                                val updatedProduct = product.copy(favorite = isLiked)

                                // Call ViewModel's toggleFavorite method to update the product
                                homeViewModel.toggleFavorite(updatedProduct)

                                Log.d(
                                    "LikeCounter",
                                    "Produit après mise à jour: ${product.id} - ${product.name} - Favorite: ${product.favorite}"
                                )

                            }
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
                        fontSize = 12.sp
                    )

                    if (product.rate != null) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {

                        Spacer(modifier = Modifier.size(16.dp))
                    }

                    Text(
                        text = if (product.rate != null) "${product.rate} ️" else "",
                        textAlign = TextAlign.End,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display price and original price (striked through if available)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, // Aligne les éléments à gauche et à droite
                    verticalAlignment = Alignment.Bottom // Aligne verticalement en bas
                ) {
                    Text(
                        text = "${product.price} €",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,

                        )

                    product.originalPrice?.let { originalPrice ->
                        if (originalPrice > product.price) {
                            Text(
                                text = "$originalPrice €",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough, // Barré le prix original

                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Displays a like counter that allows the user to toggle the like status of a product.
     *
     * @param likeCount The current number of likes for the product.
     * @param isLiked The current like status of the product.
     * @param onLikeClick The callback function to be called when the like button is clicked.
     */
    @Composable
    fun LikeCounter(likeCount: Int, isLiked: Boolean, onLikeClick: () -> Unit) {
        Row(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable {
                    onLikeClick()  // Call the onLikeClick callback when clicked
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Like icon that changes based on the 'isLiked' state
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like Icon",
                tint = if (isLiked) Color.Red else Color.Black,
                modifier = Modifier.size(24.dp)  // Taille de l'icône
            )


            Spacer(modifier = Modifier.width(6.dp))

            // Display the number of likes
            Text(
                text = likeCount.toString(),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}






