package com.nedrysystems.joiefull.ui.detail

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.ui.component.ProductCard
import com.nedrysystems.joiefull.ui.component.ReviewSection
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader

import javax.inject.Inject


/**
 * DetailLayout is responsible for displaying the detailed view of a product.
 * It retrieves product information from the [DetailViewModel] and manages UI elements
 * such as product details, favorite status, reviews, and user interactions.
 *
 * @property imageLoader An instance of [ImageLoader] used for loading product images.
 * @property productId The unique identifier of the product being displayed.
 */
class DetailLayout @Inject constructor(
    private val imageLoader: ImageLoader,
    private val productId: Int
) {

    /**
     * Renders the detailed product screen, including product information, image, and review section.
     *
     * @param productId The ID of the product to be displayed.
     * @param detailViewModel The [DetailViewModel] responsible for fetching and managing product details.
     * @param imageLoader The [ImageLoader] used to fetch and display product images.
     * @param navController The [NavHostController] for handling navigation within the app.
     */
    @Composable
    fun Render(
        productId: Int,
        detailViewModel: DetailViewModel,
        imageLoader: ImageLoader,
        navController: NavHostController
    ) {
        // Observes product state and UI state from ViewModel
        val productState by detailViewModel.productState.collectAsState()
        val uiState by detailViewModel.uiState.collectAsState()

        // Fetch product details when the screen is launched
        LaunchedEffect(productId) {
            detailViewModel.fetchProductDetails(productId)
        }

        // Show loading indicator if data is still loading
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            return
        }

        // Show error message if an error occurred
        if (uiState.error.isNotEmpty()) {
            // Afficher un message d'erreur
            Text(text = "Erreur: ${uiState.error}", color = Color.Red)
            return
        }

        // Retrieve the product from state
        val product = productState

        // Show message if product is not found
        if (product == null) {
            Text(text = "Produit non trouvé")
            return
        }

        // Mutable states for managing UI interactions
        var rating by rememberSaveable { mutableStateOf(0) }
        var comment by rememberSaveable { mutableStateOf("") }
        var likeCount by remember(product) { mutableStateOf(product.likes) }
        var isLiked by remember(product) { mutableStateOf(product.favorite) }

        // Callback for handling favorite (like) button click// Callback for handling favorite (like) button click
        val onLikeClick: () -> Unit = {
            // Toggle the favorite status and update the like count
            val updatedFavoriteStatus = !isLiked
            likeCount =
                if (updatedFavoriteStatus) likeCount + 1 else likeCount - 1

            // Create a new product with updated favorite status
            val updatedProduct = product.copy(favorite = isLiked)

            // Call ViewModel's toggleFavorite method to update the product
            detailViewModel.toggleFavorite(updatedProduct)

            // Update local state 'isLiked' with new value
            isLiked = updatedFavoriteStatus
        }

        val profileImage = painterResource(id = R.drawable.esprits_criminels_)

        Log.d("RatingUpdate", "Note actuelle dans Render: $rating")

        val shareContent by detailViewModel.shareContent.observeAsState()
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            Box {
                // Product details and like button
                ProductCard(
                    product = product,
                    imageLoader = imageLoader,
                    likeCount = likeCount,
                    onProductClick = {},
                    onLikeClick = onLikeClick,
                    imageModifier = Modifier.height(535.dp),
                    boxModifier = Modifier.height(535.dp),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
                // Icons positioned at the top of the ProductCard
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { detailViewModel.prepareShareContent(product) }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Partager",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }


            // Observe shareContent and launch share intent
            shareContent?.let { shareText ->
                LaunchedEffect(shareText) {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(intent, "Partager via"))
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
                    .wrapContentHeight(),
            )
            {
                Text(
                    text = product.picture.description,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                )
            }

            // Review section where users can submit a rating and comment
            ReviewSection(
                profileImage = profileImage,
                rating = rating,
                onRatingChanged = { newRating ->
                    Log.d("RatingUpdate", "Nouvelle note dans Render: $newRating")
                    rating = newRating
                },
                comment = comment,
                onCommentChanged = { newComment -> comment = newComment }
            )
        }
    }
}