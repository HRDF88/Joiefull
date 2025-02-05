package com.nedrysystems.joiefull.utils.isTablet

import android.content.Context

/**
 * Object that provides functionality to determine if the device is a tablet.
 * This is based on the smallest screen width (in dp) of the device's configuration.
 */
object DetermineTablet {

    /**
     * Determines if the current device is a tablet.
     * A device is considered a tablet if its smallest screen width (in dp) is greater than or equal to 600dp.
     *
     * @param context The [Context] used to access the device's resources.
     * @return True if the device is considered a tablet, false otherwise.
     */
    fun isTablet(context: Context): Boolean {
        return context.resources.configuration.smallestScreenWidthDp >= 600
    }
}