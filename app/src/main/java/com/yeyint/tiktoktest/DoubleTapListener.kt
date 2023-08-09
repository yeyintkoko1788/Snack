package com.yeyint.tiktoktest

import android.view.View

class DoubleTapListener : View.OnClickListener {
    private var lastClickTime: Long = 0
    override fun onClick(v: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_TAP_TIME_DELTA) {
            // Double tap detected
            // Implement your logic here
        }
        lastClickTime = clickTime
    }

    companion object {
        private const val DOUBLE_TAP_TIME_DELTA: Long = 300 // Time in milliseconds
    }
}

