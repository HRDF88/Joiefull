package com.nedrysystems.joiefull.ui.home


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.ui.component.CategoryHeader
import com.nedrysystems.joiefull.ui.component.ProductCard
import com.nedrysystems.joiefull.ui.detail.DetailLayout
import com.nedrysystems.joiefull.utils.accessibility.AccessibilityHelper
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import com.nedrysystems.joiefull.utils.isTablet.DetermineTablet
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
    fun Render(
        viewModel: HomeViewModel,
        navController: NavHostController,
    ) {
        val uiState by viewModel.uiState.collectAsState()


        // Get the current context for Toast notifications
        val context = LocalContext.current

        //Determine is app running on tablet or phone
        val isTablet = DetermineTablet.isTablet(context)

        // get Id on the product clicked
        var selectedProductId by remember { mutableStateOf<Int?>(null) }


        // Show error message if an error occurred
        uiState.error?.let { errorResId ->
            val errorMessage = stringResource(id = errorResId)

            LaunchedEffect(errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        uiState.errorMessage?.let { exceptionMessage ->
            Text(
                text = exceptionMessage,
                color = Color.Red
            )
            return
        }

        // Reload state when screen is revisited
        LaunchedEffect(key1 = uiState.products) {
            viewModel.refreshProducts()
        }
        //Val for content Description
        val lazyColumTextContentDescription = stringResource(R.string.list_of_product_category)
        val addFavoriteClickedTextContentDescription = stringResource(R.string.addFavoriteClicked)
        val removeFavoriteClickedTextForContentDescription =
            stringResource(R.string.removeFavoriteClicked)

        Row() {
            // Main layout displaying products
            Column(
                modifier = Modifier
                    .weight(if (isTablet && selectedProductId != null) 0.55f else 1f)
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
                        LazyColumn(modifier = Modifier.semantics {
                            contentDescription = lazyColumTextContentDescription
                        }) {
                            productsGroupedByCategory.forEach { (category, products) ->
                                item {
                                    // Display category header
                                    if (isTablet) {
                                        CategoryHeader(
                                            category = category,
                                            textStyle = TextStyle(fontSize = 48.sp)
                                        )
                                    } else {

                                        CategoryHeader(category = category)
                                    }
                                }

                                item {
                                    val categoryTextContentDescription =
                                        stringResource(R.string.category_products, category)
                                    // LazyRow for displaying products
                                    LazyRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .semantics {
                                                contentDescription =
                                                    categoryTextContentDescription
                                            },
                                        contentPadding = PaddingValues(horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(products) { product ->

                                            var likeCount by rememberSaveable(product.id) {
                                                mutableStateOf(
                                                    product.likes
                                                )
                                            }

                                            var isLiked by remember(product.id) {
                                                mutableStateOf(
                                                    product.favorite
                                                )
                                            }

                                            val onLikeClick: () -> Unit = {

                                                // Toggle the favorite status and update the like count
                                                val updatedFavoriteStatus = !isLiked

                                                // Create a new product with updated favorite status
                                                val updatedProduct =
                                                    product.copy(favorite = isLiked)

                                                // Call ViewModel's toggleFavorite method to update the product
                                                viewModel.toggleFavorite(updatedProduct)

                                                // Update local state 'isLiked' with new value
                                                isLiked = updatedFavoriteStatus

                                                AccessibilityHelper.announce(
                                                    context, if (isLiked) {
                                                        addFavoriteClickedTextContentDescription
                                                    } else {
                                                        removeFavoriteClickedTextForContentDescription
                                                    }
                                                )

                                            }

                                            val onProductClick: () -> Unit = {
                                                if (isTablet) {
                                                    selectedProductId =
                                                        product.id
                                                } else {
                                                    navController.navigate("productDetail/${product.id}") // Navigue sur mobile
                                                }
                                            }

                                            // Display ProductCard

                                            if (isTablet) {
                                                ProductCard(
                                                    product = product,
                                                    imageLoader = imageLoader,
                                                    likeCount = likeCount,
                                                    onLikeClick = onLikeClick,
                                                    onProductClick = onProductClick,
                                                    imageModifier = Modifier
                                                        .height(250.dp)
                                                        .width(250.dp),
                                                    textStyle = TextStyle(fontSize = 24.sp),
                                                    boxModifier = Modifier
                                                        .height(300.dp)
                                                        .fillMaxWidth(),
                                                    starModifier = Modifier.size(24.dp),
                                                    modifier = Modifier
                                                        .padding(end = 16.dp)
                                                        .clickable(onClick = onProductClick)


                                                )
                                            } else {


                                                ProductCard(
                                                    product = product,
                                                    imageLoader = imageLoader,
                                                    likeCount = likeCount,
                                                    onLikeClick = onLikeClick,
                                                    onProductClick = onProductClick,
                                                    boxModifier = Modifier.fillMaxWidth(),
                                                    imageModifier = Modifier
                                                        .height(200.dp)
                                                        .width(200.dp),
                                                    modifier = Modifier
                                                        .padding(end = 8.dp)
                                                        .clickable(onClick = onProductClick)


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
            // Display DetailLayout at Right in the screen
            if (isTablet && selectedProductId != null) {
                Column(
                    modifier = Modifier
                        .weight(0.45f) // Size % screen
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    selectedProductId?.let { productId ->
                        DetailLayout(
                            productId = productId,
                            imageLoader = imageLoader
                        ).Render(
                            productId = productId,
                            detailViewModel = hiltViewModel(),
                            imageLoader = imageLoader,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
