package com.nedrysystems.joiefull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.nedrysystems.joiefull.ui.home.HomeLayout
import com.nedrysystems.joiefull.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MainActivity is the entry point of the application and is responsible for initializing the UI and handling user interactions.
 * It extends [ComponentActivity] and leverages [Hilt] for dependency injection to obtain instances of [HomeViewModel] and [HomeLayout].
 * The activity sets up the UI using Jetpack Compose and displays the main layout through [HomeLayout].
 *
 * @property homeViewModel The [HomeViewModel] instance injected by Hilt, responsible for providing the data and handling business logic for the main screen.
 * @property homeLayout The [HomeLayout] instance injected by Hilt, which is responsible for rendering the UI based on the data provided by the [HomeViewModel].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Inject the HomeViewModel using Hilt
    private val homeViewModel: HomeViewModel by viewModels()

    // Inject HomeLayout here
    @Inject
    lateinit var homeLayout: HomeLayout

    /**
     * Called when the activity is first created. Sets up the content view using Jetpack Compose,
     * and calls the `Render` method of [HomeLayout] to display the UI.
     * The [HomeViewModel] is passed as a parameter to the [homeLayout.Render] method.
     *
     * @param savedInstanceState A [Bundle] containing the activity's previously saved state. If the activity has never been started, this is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Appeler la méthode Render de homeLayout injecté par Hilt
                homeLayout.Render(viewModel = homeViewModel)
            }
        }
    }
}
