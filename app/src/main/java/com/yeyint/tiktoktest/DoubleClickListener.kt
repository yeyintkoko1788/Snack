package com.yeyint.tiktoktest

import android.view.View


abstract class DoubleClickListener : View.OnClickListener {
    var lastClickTime: Long = 0
    override fun onClick(p0: View?) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(p0)
        } else {
            onSingleClick(p0)
        }
        lastClickTime = clickTime
    }

    abstract fun onSingleClick(v: View?)
    abstract fun onDoubleClick(v: View?)

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
    }
}