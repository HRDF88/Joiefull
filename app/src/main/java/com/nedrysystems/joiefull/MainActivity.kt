package com.nedrysystems.joiefull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nedrysystems.joiefull.ui.home.HomeLayout
import com.nedrysystems.joiefull.ui.home.HomeViewModel
import com.nedrysystems.joiefull.ui.splashScreen.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MainActivity is the entry point of the application, responsible for initializing the user interface (UI)
 * and handling user interactions. It extends [ComponentActivity] and uses [Hilt] for dependency injection
 * to obtain instances of [HomeViewModel] and [HomeLayout].
 * This activity sets up the UI using Jetpack Compose and displays the main layout via [HomeLayout].
 *
 * @property homeViewModel The [HomeViewModel] instance injected by Hilt, responsible for providing data and
 * handling business logic for the main screen.
 * @property homeLayout The [HomeLayout] instance injected by Hilt, responsible for rendering the UI based on
 * the data provided by the [HomeViewModel].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var showSplash by mutableStateOf(true)

    // Inject the HomeViewModel using Hilt
    private val homeViewModel: HomeViewModel by viewModels()

    // Inject HomeLayout here
    @Inject
    lateinit var homeLayout: HomeLayout

    /**
     * Called when the activity is first created. Sets up the content view using Jetpack Compose.
     * If [showSplash] is true, it shows the splash screen; otherwise, it displays the main layout through
     * [homeLayout.Render], passing the [HomeViewModel] instance as a parameter.
     *
     * @param savedInstanceState A [Bundle] containing the activity's previously saved state.
     *                           If the activity has never been started, this will be null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Display splash screen if showSplash is true
                if (showSplash) {
                    SplashScreen(
                        onFinish = {
                            showSplash = false // Set to false to navigate to the main screen
                        }
                    )
                } else {
                    // Display the main content after splash screen
                    homeLayout.Render(viewModel = homeViewModel)
                }
            }
        }
    }
}