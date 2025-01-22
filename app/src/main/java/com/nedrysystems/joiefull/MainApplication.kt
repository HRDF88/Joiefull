package com.nedrysystems.joiefull

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class that serves as the entry point for the application.
 * It is annotated with `@HiltAndroidApp` to enable Hilt dependency injection
 * throughout the app. This class is required for setting up Hilt in an Android project.
 */
@HiltAndroidApp
class MainApplication : Application(){}