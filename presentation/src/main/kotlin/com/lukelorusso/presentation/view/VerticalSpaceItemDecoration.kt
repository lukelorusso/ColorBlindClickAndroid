package com.lukelorusso.presentation.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(
        private val verticalSpaceHeight: Float,
        private val spaceFirst: Boolean = true,
        private val spaceLast: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (!spaceFirst && position == 0) outRect.bottom = 0
        else if (!spaceLast && position == parent.adapter?.itemCount?.minus(1)) outRect.bottom = 0
        else outRect.bottom = Math.round(verticalSpaceHeight)
    }
}
