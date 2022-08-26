package com.riverlet.browser.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View


@SuppressLint("ClickableViewAccessibility")
fun View.setSimpleClickEffect() {
    this.setOnTouchListener { v, event ->{}
        if (event.action == MotionEvent.ACTION_DOWN) {
            v.alpha = 0.5f
        } else {
            v.alpha = 1.0f
        }

        false
    }
}