package com.example.popupsubtitledemo.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AccessibilityService : AccessibilityService() {
    private val info = AccessibilityServiceInfo()

    override fun onServiceConnected() {
        Log.d("XXX", "Connected")
        info.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
            packageNames = arrayOf(NETFLIX_PACKAGE_NAME)
            notificationTimeout = 100
        }
        this.serviceInfo = info
    }

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val remainTimeTextList = event?.source?.findAccessibilityNodeInfosByViewId("$NETFLIX_PACKAGE_NAME:id/$REMAIN_TIME_TEXT_VIEW_ID")
        remainTimeTextList?.let {
            for (remainTimeText in it) {
                Log.d("XXX", "${remainTimeText.text}")
            }
        }
//        val currentTimeTextList = event?.source?.findAccessibilityNodeInfosByViewId("$NETFLIX_PACKAGE_NAME:id/$CURRENT_TIME_TEXT_VIEW_ID")
//        currentTimeTextList?.let {
//            for (currentTimeText in it) {
//                Log.d("XXX", "${currentTimeText.text}")
//            }
//        }
    }

    companion object {
        private const val NETFLIX_PACKAGE_NAME = "com.netflix.mediaclient"
        private const val REMAIN_TIME_TEXT_VIEW_ID = "label_time_remaining"
        private const val CURRENT_TIME_TEXT_VIEW_ID = "bif_current_time_label"
        private const val TIME_SEEK_BAR_ID = "timeline_seekbar"
    }
}