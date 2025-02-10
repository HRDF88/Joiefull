package com.nedrysystems.joiefull.utils.accessibility

import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager

object AccessibilityHelper {
    fun announce(context: Context, message: String) {
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (accessibilityManager.isEnabled) {
            accessibilityManager.interrupt() // Coupe toute annonce en cours
            accessibilityManager.sendAccessibilityEvent(
                AccessibilityEvent.obtain().apply {
                    eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
                    text.add(message)
                }
            )
        }
    }
}
