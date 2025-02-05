package com.nedrysystems.joiefull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nedrysystems.joiefull.ui.detail.DetailLayout
import com.nedrysystems.joiefull.ui.home.HomeLayout
import com.nedrysystems.joiefull.ui.home.HomeViewModel
import com.nedrysystems.joiefull.ui.splashScreen.SplashScreen
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * MainActivity serves as the entry point of the application. It is responsible for:
 * - Initializing the user interface (UI) using Jetpack Compose.
 * - Managing navigation between screens using [NavHostController].
 * - Displaying a splash screen before transitioning to the main content.
 *
 * This activity extends [ComponentActivity] and leverages Hilt for dependency injection
 * to obtain instances of [HomeViewModel], [HomeLayout], and [ImageLoader].
 *
 * @property homeViewModel The [HomeViewModel] instance injected by Hilt, responsible for
 * providing data and handling business logic for the main screen.
 * @property homeLayout The [HomeLayout] instance injected by Hilt, responsible for rendering
 * the UI based on the data provided by the [HomeViewModel].
 * @property imageLoader The [ImageLoader] instance injected by Hilt, used for efficiently
 * loading and caching images throughout the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * A mutable state variable that determines whether the splash screen is displayed.
     * It is initially set to `true` and updated after a delay.
     */

    private var showSplash by mutableStateOf(true)

    /**
     * ImageLoader instance injected by Hilt, used for loading images efficiently.
     */
    @Inject
    lateinit var imageLoader: ImageLoader

    /**
     * Called when the activity is first created. This method initializes the UI,
     * sets up navigation, and handles the splash screen display logic.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down, this Bundle contains the data it most recently supplied.
     * Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Create the NavController within the composable function
            val navController = rememberNavController()

            MaterialTheme {
                // Display the splash screen while showSplash is true
                LaunchedEffect(showSplash) {
                    if (showSplash) {
                        delaySplashScreen()
                    }
                }

                // Display SplashScreen if showSplash is true, otherwise display navigation
                if (showSplash) {
                    SplashScreen(onFinish = { showSplash = false })
                } else {
                    // Once the splash screen is completed, display the main navigation
                    JoiefullNavHost(navHostController = navController, imageLoader = imageLoader)
                }
            }
        }
    }

    /**
     * Suspended function to introduce a delay for the splash screen.
     * It waits for a predefined duration before updating `showSplash` to `false`.
     */
    private suspend fun delaySplashScreen() {
        delay(2000) // Wait for 2 seconds before hiding the splash screen
        showSplash = false
    }

}

/**
 * JoiefullNavHost is responsible for setting up the navigation structure of the app.
 * It defines different routes using Jetpack Compose's [NavHost] and handles transitions
 * between the home screen and product detail screens.
 *
 * @param navHostController The [NavHostController] instance responsible for managing
 * navigation between different composable screens.
 * @param imageLoader The [ImageLoader] instance used to load images efficiently.
 */
@Composable
fun JoiefullNavHost(navHostController: NavHostController, imageLoader: ImageLoader) {

    // Create the ViewModel within the composable scope to ensure the correct context
    val homeViewModel: HomeViewModel = hiltViewModel()

    NavHost(navController = navHostController, startDestination = "home") {
        composable("home") {
            // Pass the ViewModel and NavController to HomeLayout for UI rendering
            HomeLayout(imageLoader = imageLoader).Render(
                viewModel = homeViewModel,
                navController = navHostController
            )
        }

        // Route to display product details, retrieving productId from navigation arguments
        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: -1

            // Call the Render function of DetailLayout with the productId
            DetailLayout(imageLoader = imageLoader, productId = productId).Render(
                productId = productId,  // The productId is passed to Render
                detailViewModel = hiltViewModel(), // Use Hilt to inject the ViewModel
                imageLoader = imageLoader,
                navController = navHostController
            )
        }
    }
}
