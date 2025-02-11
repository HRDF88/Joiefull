package com.nedrysystems.joiefull.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.ui.component.ProductCard
import com.nedrysystems.joiefull.ui.component.ReviewSection
import com.nedrysystems.joiefull.ui.component.SaveButton
import com.nedrysystems.joiefull.utils.accessibility.AccessibilityHelper
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import com.nedrysystems.joiefull.utils.isTablet.DetermineTablet
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

        val context = LocalContext.current
        //Determine is app running on tablet or phone
        val isTablet = DetermineTablet.isTablet(context)


        // Fetch product details and user's profile picture when the screen is launched
        LaunchedEffect(productId) {
            detailViewModel.fetchProductDetails(productId)
            detailViewModel.getUserAndUpdateState()
        }

        // Show loading indicator if data is still loading
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            return
        }

        // Show error message if an error occurred
        uiState.error?.let { errorResId ->
            val errorMessage = stringResource(id = errorResId)
            Text(
                text = errorMessage,
                color = Color.Red
            )
            return
        }

        uiState.errorMessage?.let { exceptionMessage ->
            Text(
                text = exceptionMessage,
                color = Color.Red
            )
            return
        }


        // Retrieve the product from state
        val product = productState
        val user = uiState.user

        // Show message if product is not found
        if (product == null) {
            Text(text = stringResource(R.string.product_not_found))
            return
        }

        // Mutable states for managing UI interactions
        var rating by rememberSaveable { mutableStateOf(0) }
        var comment by rememberSaveable { mutableStateOf("") }
        val likeCount by remember(product) { mutableStateOf(product.likes) }
        var isLiked by remember(product) { mutableStateOf(product.favorite) }

        //Content Description string resources ans string resources
        val addFavoriteClickedTextContentDescription = stringResource(R.string.addFavoriteClicked)
        val removeFavoriteClickedTextForContentDescription =
            stringResource(R.string.removeFavoriteClicked)
        val errorToastSaveButtonText = stringResource(R.string.error_save_buton)
        val reviewSavedText = stringResource(R.string.review_save)
        val shareByText = stringResource(R.string.share_by)

        // Callback for handling favorite (like) button click// Callback for handling favorite (like) button click
        val onLikeClick: () -> Unit = {
            // Toggle the favorite status and update the like count
            val updatedFavoriteStatus = !isLiked


            // Create a new product with updated favorite status
            val updatedProduct = product.copy(favorite = isLiked)

            // Call ViewModel's toggleFavorite method to update the product
            detailViewModel.toggleFavorite(updatedProduct)

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

        // user's profile image
        val profileImage = user?.let {
            imageLoader.loadImagePainter(
                url = it.picture,
                placeholder = R.drawable.error,
                error = R.drawable.error
            )
        }

        val shareContent by detailViewModel.shareContent.observeAsState()


        // Callback for SaveButton
        val saveButtonClick: () -> Unit = {
            if (rating == 0 || comment.isEmpty()) { // Checks if the rating is 0 or if the comment is empty
                // Show a Toast if no note is selected or the comment is empty
                Toast.makeText(
                    context,
                    errorToastSaveButtonText,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Run the saveReviewAndUpdateProductRating function with the appropriate parameters if everything is valid
                detailViewModel.saveReviewAndUpdateProductRating(1, productId, rating, comment)
                // Show a Toast to inform user about success
                Toast.makeText(
                    context,
                    reviewSavedText,
                    Toast.LENGTH_SHORT
                ).show()
                // return to Home
                navController.popBackStack()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            item {
                Box {
                    // Product details and like button
                    ProductCard(
                        product = product,
                        imageLoader = imageLoader,
                        likeCount = likeCount,
                        onProductClick = {},
                        onLikeClick = onLikeClick,
                        imageModifier = Modifier.height(500.dp),
                        boxModifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 18.sp),
                        starModifier = Modifier.size(24.dp),
                        cardElevation = 0.dp,
                        cropImage = ContentScale.Fit
                    )
                    // Icons positioned at the top of the ProductCard
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        IconButton(onClick = { navController.popBackStack() }) {
                            if (!isTablet) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        IconButton(onClick = {
                            detailViewModel.prepareShareContent(product)
                            Handler(Looper.getMainLooper()).postDelayed({
                                // Now recreate the activity after a delay so the share has time to prepare
                                (context as? Activity)?.recreate()
                            }, 2000)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = stringResource(R.string.share_product),
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Bottom)
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
                        context.startActivity(Intent.createChooser(intent, shareByText))
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
                        modifier = Modifier.semantics { invisibleToUser() }

                    )
                }

                // Review section where users can submit a rating and comment
                if (profileImage != null) {
                    ReviewSection(
                        profileImage = profileImage,
                        rating = rating,
                        onRatingChanged = { newRating ->
                            rating = newRating
                        },
                        comment = comment,
                        onCommentChanged = { newComment -> comment = newComment }
                    )
                }
                SaveButton(saveButtonClick = saveButtonClick, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}