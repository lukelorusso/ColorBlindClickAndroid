package com.lukelorusso.presentation_old.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

class MaybeScrollableViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var initialXValue: Float = 0.toFloat()
    var direction: SwipeDirection

    enum class SwipeDirection {
        ALL, LEFT, RIGHT, NONE
    }

    init {
        direction = SwipeDirection.ALL
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isSwipeAllowed(event)) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (isSwipeAllowed(event)) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    private fun isSwipeAllowed(event: MotionEvent): Boolean {
        if (this.direction == SwipeDirection.ALL) return true

        if (direction == SwipeDirection.NONE)
        //disable any swipe
            return false

        if (event.action == MotionEvent.ACTION_DOWN) {
            initialXValue = event.x
            return true
        }

        if (event.action == MotionEvent.ACTION_MOVE) {
            try {
                val diffX = event.x - initialXValue
                if (diffX > 0 && direction == SwipeDirection.RIGHT) {
                    // swipe from LEFT to RIGHT detected
                    return false
                } else if (diffX < 0 && direction == SwipeDirection.LEFT) {
                    // swipe from RIGHT to LEFT detected
                    return false
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

        }

        return true
    }

}
