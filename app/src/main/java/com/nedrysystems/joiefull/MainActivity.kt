package com.nedrysystems.joiefull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import com.nedrysystems.joiefull.ui.home.HomeLayout
import com.nedrysystems.joiefull.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Inject the HomeViewModel using Hilt
    private val homeViewModel: HomeViewModel by viewModels()

    // Inject HomeLayout here
    @Inject
    lateinit var homeLayout: HomeLayout

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
